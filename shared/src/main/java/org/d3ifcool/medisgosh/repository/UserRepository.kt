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
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.ResponseStatus

class UserRepository(
    private val isEmployee: Boolean,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
) {
    private var registration: ListenerRegistration? = null

    suspend fun changePfp(
        image: ByteArray,
    ): ResponseStatus {
        val storageRef =
            storage.reference.child("${User.COLLECTION}/${auth.currentUser?.uid ?: "unknown"}/profile/${AppHelper.getShortUUID()}.jpeg")
        return try {
            storageRef.putBytes(image).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            if (auth.currentUser!!.photoUrl != null) {
                try {
                    val photoUrl = auth.currentUser!!.photoUrl.toString()
                    val path =
                        photoUrl.substringAfter("/o/").substringBefore("?").replace("%2F", "/")
                    storage.reference.child(path).delete().await()
                } catch (e: Exception) {
                    Log.e("Repo", "Error: ${e.message}", e)
                    Log.e("UserRepository", "Error: ${e.message}", e)
                    ResponseStatus.FAILED.apply {
                        updateMessage("Failed: ${e.message}")
                    }
                }
            }
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(downloadUrl)
            }
            auth.currentUser?.updateProfile(profileUpdates)?.await()
            ResponseStatus.SUCCESS.apply {
                updateMessage("Success!")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("UserRepository", "Error: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage("Failed: ${e.message}")
            }
        }
    }

    suspend fun editProfile(
        user: User,
        image: ByteArray? = null
    ): ResponseStatus {
        return try {
            var downloadUrl: String? = null

            // Upload image
            if (image != null) {
                val storageRef = storage.reference.child(
                    "${User.COLLECTION}/${auth.currentUser?.uid ?: "unknown"}/profile/${AppHelper.getShortUUID()}.jpeg"
                )

                storageRef.putBytes(image).await()
                downloadUrl = storageRef.downloadUrl.await().toString()

                // Delete old profile picture if it exists
                auth.currentUser?.photoUrl?.let { uri ->
                    try {
                        val photoUrl = uri.toString()
                        val path =
                            photoUrl.substringAfter("/o/").substringBefore("?").replace("%2F", "/")
                        storage.reference.child(path).delete().await()
                    } catch (e: Exception) {
                        Log.e("Repo", "Failed to delete old image: ${e.message}", e)
                    }
                }

                // Update Firebase Auth profile
                val profileUpdates = userProfileChangeRequest {
                    displayName = user.name
                    photoUri = Uri.parse(downloadUrl)
                }

                auth.currentUser?.updateProfile(profileUpdates)?.await()
            }
            // Prepare and store user metadata
            val userMetadata = user.copy(
                photoUrl = downloadUrl,
                hasDoneOnboarding = true,
            )

            db.collection(User.COLLECTION)
                .document(auth.currentUser!!.uid)
                .set(userMetadata)
                .await()

            ResponseStatus.SUCCESS.apply {
                updateMessage("Data baru telah disimpan")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("UserRepository", "Error: ${e.message}", e)
            ResponseStatus.FAILED.apply {
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
            db.collection(User.COLLECTION).document(id).get().await().toObject(User::class.java)
                ?.copy(id = id)
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("UserRepository", "Error: ${e.message}", e)
            null
        }
    }

    suspend fun signUp(username: String, email: String, password: String): ResponseStatus {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            user?.let {
                try {
                    // Update display name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()
                    user.updateProfile(profileUpdates).await()

                    // Create User data model
                    val userData = User(
                        id = user.uid,
                        name = username,
                        email = user.email,
                        photoUrl = user.photoUrl?.toString(),
                        role = User.CLIENT_ROLE,
                        hasDoneOnboarding = false,
                    )

                    db.collection(User.COLLECTION)
                        .document(user.uid)
                        .set(userData)
                        .await()

                    ResponseStatus.SUCCESS.apply {
                        updateMessage("Success!")
                    }
                } catch (e: Exception) {
                    Log.e("Repo", "Error during profile update or Firestore write: ${e.message}", e)
                    ResponseStatus.FAILED.apply {
                        updateMessage("Failed: ${e.message}")
                    }
                }
            } ?: run {
                ResponseStatus.FAILED.apply {
                    updateMessage("Failed: User is null after creation")
                }
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error during sign up: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage("Failed: ${e.message}")
            }
        }
    }

    suspend fun signIn(username: String, password: String): ResponseStatus {
        return try {
            auth.signInWithEmailAndPassword(username, password).await()
            ResponseStatus.SUCCESS.apply {
                updateMessage("Login Berhasil")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("LoginViewModel", "Error: ${e.message}", e)

            val message = when {
                e.message?.contains("incorrect", ignoreCase = true) == true ->
                    "Email atau Kata Sandi salah"

                e.message?.contains("format", ignoreCase = true) == true ->
                    "Format Email tidak benar"

                e.message?.contains("no user record", ignoreCase = true) == true ->
                    "Pengguna tidak ditemukan"

                e.message?.contains("network error", ignoreCase = true) == true ->
                    "Tidak ada koneksi internet"

                else -> e.message ?: "Terjadi Kesalahan."
            }

            ResponseStatus.FAILED.apply {
                updateMessage("Login gagal: $message")
            }
        }
    }

    suspend fun signOut(): ResponseStatus {
        return try {
            auth.signOut()
            ResponseStatus.SUCCESS.apply {
                updateMessage("Berhasil Keluar")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("LoginViewModel", "Error: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage(e.message ?: "Terjadi Kesalahan.")
            }
        }
    }

    suspend fun submitForgotPassword(email: String): ResponseStatus {
        return try {
            auth.sendPasswordResetEmail(email).await()
            ResponseStatus.SUCCESS.apply {
                updateMessage("Link reset kata sandi telah dikirim ke email.")
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("ForgotPasswordVM", "Email not found: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage("Email tidak ditemukan. Pastikan email sudah terdaftar.")
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ForgotPasswordVM", "Invalid email format: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage("Format email tidak valid.")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("ForgotPasswordVM", "Error: ${e.message}", e)

            val message = when {
                e.message?.contains("network error", ignoreCase = true) == true ->
                    "Tidak ada koneksi internet."

                e.message?.contains("badly formatted", ignoreCase = true) == true ->
                    "Format email tidak valid."

                else -> e.message ?: "Terjadi kesalahan saat mengirim reset password."
            }

            ResponseStatus.FAILED.apply {
                updateMessage("Gagal mengirim email reset: $message")
            }
        }
    }

    suspend fun handleSignInResult(): ResponseStatus {
        return try {
            val user = auth.currentUser
                ?: return ResponseStatus.FAILED.apply { updateMessage("Tidak ada pengguna yang ditemukan.") }

            val userDocSnapshot = db.collection(User.COLLECTION)
                .document(user.uid)
                .get()
                .await()

            if (userDocSnapshot.exists()) {
                val existingUser = userDocSnapshot.toObject(User::class.java)
                val expectedRole = if (isEmployee) User.EMPLOYEE_ROLE else User.CLIENT_ROLE

                if (existingUser?.role != expectedRole) {
                    auth.signOut() // logout user
                    return ResponseStatus.FAILED.apply {
                        updateMessage("Akses ditolak: Role pengguna tidak sesuai.")
                    }
                }
            } else {
                val userData = User(
                    id = user.uid,
                    name = user.displayName,
                    email = user.email,
                    photoUrl = user.photoUrl?.toString(),
                    role = if (isEmployee) User.EMPLOYEE_ROLE else User.CLIENT_ROLE,
                    hasDoneOnboarding = false,
                )

                db.collection(User.COLLECTION)
                    .document(user.uid)
                    .set(userData)
                    .await()
            }

            ResponseStatus.SUCCESS.apply {
                updateMessage("Google Sign-In Berhasil!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseStatus.FAILED.apply {
                updateMessage("Google Sign-In Gagal: ${e.localizedMessage}")
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