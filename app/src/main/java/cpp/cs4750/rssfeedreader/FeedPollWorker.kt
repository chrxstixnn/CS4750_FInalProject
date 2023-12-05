package cpp.cs4750.rssfeedreader

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cpp.cs4750.rssfeedreader.repository.FeedRepository
import cpp.cs4750.rssfeedreader.repository.PreferencesRepository
import kotlinx.coroutines.flow.first

class FeedPollWorker (
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val preferencesRepository = PreferencesRepository.get()
        val feedRepository = FeedRepository.get()


        val query = preferencesRepository.storedQuery.first()
        val lastFeed = preferencesRepository.lastFeedId.first()
        notifyReader()

        if(query.isEmpty()){
            return Result.success()
        }



        return Result.success()
    }




    @SuppressLint("MissingPermission")
    private fun notifyReader(){
        val intent = MainActivity.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val resources = context.resources

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setContentTitle("New notification")
            .setContentText("New RSS feed articles")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        NotificationManagerCompat.from(context).notify(0, notification)
        
    }
}