package org.d3ifcool.medisgosh.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgosh.util.ResponseStatus
import org.d3ifcool.medisgosh.util.MediaUtils

class OrderRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val uid: String,
    private val storage: FirebaseStorage,
) {
    private var registration: ListenerRegistration? = null

    fun getOrders(
        isEmployee: Boolean,
        onDataUpdated: (List<Order>?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val collection = if (isEmployee) Order.FIELD_EMPLOYEE_ID else Order.FIELD_CLIENT_ID
        registration = db.collection(Order.COLLECTION)
            .whereEqualTo(collection, auth.currentUser!!.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.map {
                    it.toObject(Order::class.java)?.copy(id = it.id) ?: Order()
                }

                onDataUpdated(orders)
            }
    }

    fun removeListener() {
        registration?.remove()
    }

    suspend fun getOrderById(id: String): Order? {
        return try {
            db.collection(Order.COLLECTION).document(id).get().await().toObject(Order::class.java)
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.e("OrderRepository", "Error: ${e.message}", e)
            null
        }
    }

    suspend fun submitOrder(order: Order, bitmap: Bitmap): ResponseStatus {
        val byteArr = MediaUtils.bitmapToByteArray(bitmap)
        return try {
            byteArr.let {
                val user = db.collection(User.COLLECTION).document(uid).get().await()
                    .toObject(User::class.java)
                val response =
                    db.collection(Order.COLLECTION).add(order.copy(client = user ?: User(id = uid)))
                        .await().get().await()
                val orderId = response?.id
                val storageRef =
                    storage.reference.child(
                        "${Order.COLLECTION}/$orderId/foto_keluhan/${AppHelper.getShortUUID()}.jpeg"
                    )
                storageRef.putBytes(it).await()
                val downloadUrl = storageRef.downloadUrl.await().toString()
                db.collection(Order.COLLECTION).document(orderId!!)
                    .set(
                        response.toObject(Order::class.java)!!
                            .copy(supportingImageUrl = downloadUrl)
                    ).await()
                ResponseStatus.SUCCESS.apply {
                    updateMessage("Janji telah dibuat!")
                }
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            Log.d("OrderRepo", "${e.message}")
            ResponseStatus.FAILED.apply {
                updateMessage("Unknown Error Occurred")
            }
        }
    }

    suspend fun submitPayment(order: Order): ResponseStatus {
        return try {
            db.collection(Order.COLLECTION).document(order.id!!)
                .set(order.copy(paymentReceiptUrl = "", status = "ONGOING")).await()
            ResponseStatus.SUCCESS.apply {
                updateMessage("Pembayaran berhasil")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage(message = e.message)
            }
        }
    }

    suspend fun cancelOrder(order: Order): ResponseStatus {
        order.supportingImageUrl?.let {
            val filePath = MediaUtils.getFilePathFromUrl(it)
                ?: return ResponseStatus.FAILED.apply {
                    updateMessage("Pesanan gagal dibatalkan")
                }

            val storageRef = storage.reference.child(filePath)

            return try {
                storageRef.delete().await()
                db.collection(Order.COLLECTION).document(order.id!!).delete().await()
                ResponseStatus.SUCCESS.apply {
                    updateMessage("Pesanan berhasil dibatalkan")
                }
            } catch (e: Exception) {
                Log.e("Repo", "Error: ${e.message}", e)
                ResponseStatus.FAILED.apply {
                    updateMessage("Pesanan gagal dibatalkan")
                }
            }
        }
        return ResponseStatus.FAILED.apply {
            updateMessage("Pesanan gagal dibatalkan")
        }
    }

    suspend fun markAsDone(order: Order): ResponseStatus {
        return try {
            db.collection(Order.COLLECTION).document(order.id!!)
                .set(order.copy(status = "COMPLETE")).await()
            ResponseStatus.SUCCESS.apply {
                updateMessage("Pesanan berhasil diselesaikan")
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
            ResponseStatus.FAILED.apply {
                updateMessage("Pesanan gagal diselesaikan")
            }
        }
    }
}