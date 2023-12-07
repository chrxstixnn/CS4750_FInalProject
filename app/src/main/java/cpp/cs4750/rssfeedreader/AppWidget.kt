package cpp.cs4750.rssfeedreader

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {

    companion object {
        // Update the widget with the given items
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, items: List<String>) {
            val views = RemoteViews(context.packageName, R.layout.app_widget)

            // Build a string with items
            val itemsText = items.joinToString(separator = "\n")

            // Update the TextView in the widget
            views.setTextViewText(R.id.widgetItemsTextView, itemsText)

            // Notify the AppWidgetManager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // This is called to update the widget periodically
        // In this example, we don't schedule periodic updates, but you can implement it if needed
    }
}