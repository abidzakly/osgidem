package org.d3ifcool.medisgosh.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID

class AppHelper {
    companion object {
        @SuppressLint("ServiceCast")
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        fun toastGenerator(context: Context, intMessage: Int? = null, strMessage: String? = null) {
            return if (intMessage != null) {
                Toast.makeText(
                    context,
                    context.getString(intMessage),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (strMessage != null) {
                Toast.makeText(
                    context,
                    strMessage,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
            }
        }

        fun navigate(
            navController: NavHostController,
            route: String,
        ) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        fun getShortUUID(): String {
            return UUID.randomUUID().toString().substring(0, 8)
        }

        fun calculateAge(dateOfBirth: String): Int {
            // Define the formatter for parsing the date string
            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            } else {
                return 0
            }
            val birthDate = LocalDate.parse(dateOfBirth, formatter) // Parse the date string
            val currentDate = LocalDate.now() // Get the current date

            // Calculate the period between birth date and current date
            return Period.between(birthDate, currentDate).years
        }

        fun stripeFormat(timestamp: Timestamp): String {
            val date = timestamp.toDate()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun hhmmFormat(timestamp: Timestamp?): String? {
            if (timestamp != null) {
                // Convert Firebase Timestamp to Date
                val date = timestamp.toDate()

                // Create time formatter for just the hour
                val hourFormatter = SimpleDateFormat("HH.mm", Locale("id", "ID"))
                val formattedHour = hourFormatter.format(date)
                return formattedHour
            } else {
                return ""
            }
        }

        fun completeFormat(timestamp: Timestamp?): String {
            if (timestamp != null) {
                // Convert Firebase Timestamp to Date
                val date = timestamp.toDate()

                // Create date formatter for Indonesian locale
                val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                val formattedDate = dateFormatter.format(date)

                // Create time formatter
                val timeFormatter = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                val formattedTime = timeFormatter.format(date)

                // Combine date and time
                return "$formattedDate, pukul $formattedTime WIB"
            }
            return ""
        }

        fun openWhatsAppWithTemplate(context: Context, phoneNumber: String, message: String) {
            try {
                Toast.makeText(context, "Waiting..", Toast.LENGTH_SHORT).show()
                // Format the phone number for WhatsApp (including country code, no '+' or leading zeroes)
                val formattedNumber = phoneNumber.replace("+", "").trim()

                // Encode the message to make it URL-safe
                val encodedMessage = Uri.encode(message)

                // Create the intent with a template message
                val uri = Uri.parse("https://wa.me/$formattedNumber?text=$encodedMessage")

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                }
                intent.setPackage("com.whatsapp")
                val chooser = Intent.createChooser(intent, "Open with:")
                context.startActivity(chooser)
            } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
                Log.e("WhatsAppIntent", "Unexpected error: ${e.message}")
                Toast.makeText(context, "An unexpected error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}