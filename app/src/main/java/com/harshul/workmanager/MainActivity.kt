package com.harshul.workmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.harshul.workmanager.databinding.ActivityMainBinding
import com.harshul.workmanager.utils.Constants
import com.harshul.workmanager.workers.ColorFilterWorker
import com.harshul.workmanager.workers.DownloadWorker
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val downloadRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        val colorFilterRequest = OneTimeWorkRequest.Builder(ColorFilterWorker::class.java)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)

        workManager.getWorkInfosForUniqueWorkLiveData("download")
            .observe(this) { workInfos ->
                val downloadInfo = workInfos?.find { it.id == downloadRequest.id }
                val filterInfo = workInfos?.find { it.id == colorFilterRequest.id }

                val downloadUri = downloadInfo?.outputData?.getString(Constants.IMAGE_URI)
                val filterUri = filterInfo?.outputData?.getString(Constants.FILTER_URI)

                val imageUri = filterUri ?: downloadUri

                Picasso.get()
                    .load(imageUri)
                    .into(binding.imageView)

            }

        binding.buttonDownload.setOnClickListener {
            workManager.beginUniqueWork("download", ExistingWorkPolicy.KEEP, downloadRequest)
                .then(colorFilterRequest)
                .enqueue()
        }

    }
}