package org.d3ifcool.medisgosh.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.AppObjectState
import org.d3ifcool.medisgosh.model.User

class UserRepository(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
) {
    private var registration: ListenerRegistration? = null

    suspend fun changePfp(
        image: ByteArray,
    ): AppObjectState {
        val storageRef =
            storage.reference.child("${User.COLLECTION}/${auth.currentUser?.uid ?: "unknown"}/profile/${AppHelper.getShortUUID()}.jpeg")
        return try {
            storageRef.putBytes(image).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            if (auth.currentUser!!.photoUrl != null) {
                try {
                    val photoUrl = auth.currentUser!!.photoUrl.toString()
                    val path = photoUrl.substringAfter("/o/").substringBefore("?").replace("%2F", "/")
                    storage.reference.child(path).delete().await()
                } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
                    Log.e("UserRepository", "Error: ${e.message}", e)
                    AppObjectState.FAILED.apply {
                        updateMessage("Failed: ${e.message}")
                    }
                }
            }
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(downloadUrl)
            }
            auth.currentUser?.updateProfile(profileUpdates)?.await()
            AppObjectState.SUCCESS.apply {
                updateMessage("Success!")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("UserRepository", "Error: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage("Failed: ${e.message}")
            }
        }
    }

    suspend fun editProfile(
        user: User,
    ): AppObjectState {
        return try {
            val profileUpdates = userProfileChangeRequest {
                displayName = user.name
            }
            try {
                auth.currentUser?.updateProfile(profileUpdates)?.await()
            } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
                Log.e("UserRepository", "Error: ${e.message}", e)
                AppObjectState.FAILED.apply {
                    updateMessage("Failed: ${e.message}")
                }
            }
            val userMetadata = user.copy(
                name = auth.currentUser!!.displayName!!,
                email = auth.currentUser!!.email!!,
                photoUrl = auth.currentUser!!.photoUrl!!.toString(),
                id = auth.currentUser!!.uid
                )
            db.collection(User.COLLECTION).document(auth.currentUser!!.uid).set(userMetadata)
                .await()
            AppObjectState.SUCCESS.apply {
                updateMessage("Success!")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("UserRepository", "Error: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage("Failed: ${e.message}")
            }
        }
    }

    fun getProfileData(
        onDataUpdated: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        registration = db.collection(User.COLLECTION)
            .document(auth.currentUser!!.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val userData = snapshot?.toObject(User::class.java) ?: User()

                onDataUpdated(userData)
            }
    }

    fun removeListener() {
        registration?.remove()
    }

    suspend fun getUserById(id: String): User? {
        return try {
            db.collection(User.COLLECTION).document(id).get().await().toObject(User::class.java)?.copy(id = id)
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("UserRepository", "Error: ${e.message}", e)
            null
        }
    }

    suspend fun signUp(username: String, email: String, password: String): AppObjectState {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            try {
                val profileUpdates =
                    UserProfileChangeRequest.Builder().setDisplayName(username).build()
                // Update the display name
                user?.updateProfile(profileUpdates)?.await()
            } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
                AppObjectState.FAILED.apply {
                    updateMessage("Failed: ${e.message}")
                }
            }
            AppObjectState.SUCCESS.apply {
                updateMessage("Success!")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage("Failed: ${e.message}")
            }
        }
    }

    suspend fun signIn(username: String, password: String): AppObjectState {
        return try {
            auth.signInWithEmailAndPassword(username, password).await()
            AppObjectState.SUCCESS.apply {
                updateMessage("Login Berhasil")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("LoginViewModel", "Error: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage(e.message ?: "Terjadi Kesalahan.")
            }
        }
    }

    suspend fun signOut(): AppObjectState {
        return try {
            auth.signOut()
            AppObjectState.SUCCESS.apply {
                updateMessage("Berhasil Keluar")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("LoginViewModel", "Error: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage(e.message ?: "Terjadi Kesalahan.")
            }
        }
    }

    suspend fun submitForgotPassword(email: String): AppObjectState {
        return try {
            val emailExists = checkIfEmailExists(email)
            if (emailExists) {
                auth.sendPasswordResetEmail(email).await()
                AppObjectState.SUCCESS
            } else {
                AppObjectState.FAILED.apply {
                    updateMessage("Email tidak ditemukan.")
                }
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("ForgotPasswordVM", "Email not found: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage("Email tidak ditemukan.")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("ForgotPasswordVM", "Error: ${e.message}", e)
            AppObjectState.FAILED.apply {
                updateMessage(e.message ?: "Terjadi Kesalahan.")
            }
        }
    }

    private suspend fun checkIfEmailExists(email: String): Boolean {
        return try {
            val result = auth.fetchSignInMethodsForEmail(email).await()
            result.signInMethods?.isNotEmpty() == true
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("ForgotPasswordVM", "Error checking email existence: ${e.message}", e)
            false // Return false in case of an error
        }
    }
}