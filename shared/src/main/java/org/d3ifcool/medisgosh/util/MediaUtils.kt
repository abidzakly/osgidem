package org.d3ifcool.medisgosh.util

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.canhub.cropper.CropImageView
import java.io.ByteArrayOutputStream

class MediaUtils {
    companion object {
        fun isImageFile(filename: String): Boolean {
            // Supported image file extensions
            val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff")

            // Extract the file extension from the filename
            val fileExtension = filename.substringAfterLast('.', "").lowercase()

            // Check if the file extension is in the list of image extensions
            return fileExtension in imageExtensions
        }

        fun getFileName(context: Context, uri: Uri): String {
            var fileName = ""
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                if (cursor.moveToFirst()) {
                    fileName = cursor.getString(nameIndex)
                }
            }
            return fileName
        }

        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                outputStream
            ) // Compress as JPEG with max quality
            return outputStream.toByteArray()
        }

        fun previewFile(context: Context, fileUrl: String, onNoAppFound: () -> Unit) {
            // Automatically detect MIME type based on file extension
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUrl)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)

            if (mimeType == null) {
                Toast.makeText(context, "Unsupported file type", Toast.LENGTH_SHORT).show()
                onNoAppFound()
                return
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(fileUrl), mimeType)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                onNoAppFound()
            }
        }

        fun getFilePathFromUrl(url: String): String? {
            return try {
                val encodedPath = url.substringAfter("/o/").substringBefore("?")
                java.net.URLDecoder.decode(encodedPath, "UTF-8") // Decode the encoded path
            } catch (e: Exception) {
            Log.e("Repo", "Error: ${e.message}", e)
                null // Return null if the URL format is invalid
            }
        }

        fun getCroppedImage(
            resolver: ContentResolver,
            result: CropImageView.CropResult
        ): Bitmap? {
            if (!result.isSuccessful) {
                return null
            }
            val uri = result.uriContent ?: return null

            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                MediaStore.Images.Media.getBitmap(resolver, uri)
            } else {
                val source = ImageDecoder.createSource(resolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }
}