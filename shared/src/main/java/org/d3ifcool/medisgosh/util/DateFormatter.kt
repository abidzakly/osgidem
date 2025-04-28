package org.d3ifcool.medisgosh.util

import android.util.Log
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateFormatter {
    companion object {
        fun combineDateAndTime(selectedDate: Calendar?, timeString: String): Timestamp? {
            return try {
                // Parse time string
                val timeFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())
                val time = timeFormatter.parse(timeString)

                // Set parsed time into the Calendar instance
                time?.let {
                    selectedDate?.set(Calendar.HOUR_OF_DAY, it.hours)
                    selectedDate?.set(Calendar.MINUTE, it.minutes)
                    selectedDate?.set(Calendar.SECOND, 0) // Reset seconds for a clean timestamp

                    // Convert to Firebase Timestamp
                    selectedDate?.let { it1 -> Timestamp(it1.time) }
                }
            } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
                e.printStackTrace()
                null
            }
        }
    }
}