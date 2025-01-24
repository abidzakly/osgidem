package org.d3ifcool.medisgosh.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.model.User

class ServiceRepository(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val uid: String,
) {
    private var registration: ListenerRegistration? = null

    fun getDoctors(
        onDataUpdated: (List<User>?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        registration = db.collection(User.COLLECTION)
            .whereNotEqualTo(User.SUBCOL_EMPLOYEE, null)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.map {  it.toObject(User::class.java)?.copy(id = it.id) ?: User() }

                onDataUpdated(users)
            }
    }

    suspend fun getDoctorById(
        id: String
    ): User? {
        try {
            return db.collection(User.COLLECTION).document(id).get().await().toObject(User::class.java)?.copy(id = id)
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            return null
        }
    }

    fun removeListener() {
        registration?.remove()
    }

}