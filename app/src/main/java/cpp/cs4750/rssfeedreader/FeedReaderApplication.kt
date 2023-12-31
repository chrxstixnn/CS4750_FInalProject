package cpp.cs4750.rssfeedreader

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import cpp.cs4750.rssfeedreader.repository.FeedRepository
import cpp.cs4750.rssfeedreader.repository.PreferencesRepository


const val NOTIFICATION_CHANNEL_ID = "rss_poll"
class FeedReaderApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.initialize(this)
        FeedRepository.initialize(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager? =
                getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}