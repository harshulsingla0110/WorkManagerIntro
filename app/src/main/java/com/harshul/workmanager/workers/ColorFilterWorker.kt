package com.harshul.workmanager.workers

import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.harshul.workmanager.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ColorFilterWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val imageFie = workerParams.inputData.getString(Constants.IMAGE_URI)
            ?.toUri()
            ?.toFile()

        delay(5000L)

        return imageFie?.let { file ->
            val bmp = BitmapFactory.decodeFile(file.absolutePath)
            val resultBmp = bmp.copy(bmp.config, true)
            val paint = Paint()
            paint.colorFilter = LightingColorFilter(0X5881E4, 1)
            val canvas = Canvas(resultBmp)
            canvas.drawBitmap(resultBmp, 0f, 0f, paint)

            withContext(Dispatchers.IO) {
                val resultImageFile = File(context.cacheDir, "new_image.jpg")
                val outputStream = FileOutputStream(resultImageFile)
                val success = resultBmp.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
                )

                if (success) {
                    Result.success(
                        workDataOf(Constants.FILTER_URI to resultImageFile.toUri().toString())
                    )
                } else Result.failure()
            }
        } ?: Result.failure()
    }

}