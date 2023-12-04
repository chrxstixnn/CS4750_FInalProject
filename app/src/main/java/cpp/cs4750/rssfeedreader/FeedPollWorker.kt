package cpp.cs4750.rssfeedreader

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.internal.notify

class FeedPollWorker (
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
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