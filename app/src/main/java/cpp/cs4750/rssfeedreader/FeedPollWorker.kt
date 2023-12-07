package cpp.cs4750.rssfeedreader

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cpp.cs4750.rssfeedreader.repository.FeedRepository

class FeedPollWorker (
    private val context: Context,
    workerParameters: WorkerParameters
    ): CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
        // TODO polling preference
        // val preferencesRepository = PreferencesRepository.get()
        val feedRepository = FeedRepository.get()

        val newFeeds = feedRepository.fetchNewItems()

        if (newFeeds.isNotEmpty()) {
            sendNotification()
        }

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification() {
        val intent = MainActivity.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // TODO use string.xml

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(androidx.loader.R.drawable.notification_icon_background)
            .setContentTitle("New Feeds")
            .setContentText("New RSS feeds have loaded")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(0, notification)
    }

}