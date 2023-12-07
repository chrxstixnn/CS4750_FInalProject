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
    ): CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
        val preferencesRepository = PreferencesRepository.get()
        val feedRepository = FeedRepository(context)


        val query = preferencesRepository.storedQuery.first()
        val last = preferencesRepository.lastResult.first()

        if(query.isEmpty()) {

            sendNotification()
            return Result.success()
        }

        return try{

            val feeds = feedRepository.getFeeds()

            if(feeds != null){
                val newLast = feeds.first().toString()

                if(newLast == (last)){
                    //nothing
                }else{
                    preferencesRepository.setLast(newLast.toString())
                }
            }
            Result.success()
        } catch(ex: Exception){
            Result.failure()
        }

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

        val resources = context.resources

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