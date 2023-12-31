package cpp.cs4750.rssfeedreader

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cpp.cs4750.rssfeedreader.repository.FeedRepository

private const val TAG = "FeedPollWorker"

class FeedPollWorker (
    private val context: Context,
    workerParameters: WorkerParameters
    ): CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
        val feedRepository = FeedRepository.get()

        val newFeeds = feedRepository.fetchNewItems()

        if (newFeeds.isNotEmpty()) {
            sendNotification()
            Log.d(TAG, "")
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