package cpp.cs4750.rssfeedreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.IllegalStateException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID

@Entity
data class Item(
    val title: String,
    val author: String,
    val description: String,
    val link: String,
    val pubDate: Date,
    val content: String,
    val read: Boolean,
    @PrimaryKey val id: UUID = UUID.randomUUID()
) {
    override fun equals(other: Any?): Boolean = (other as? Item)?.let {
        this.title == it.title &&
                this.author == it.author &&
                this.pubDate == it.pubDate
    } ?: false
}

fun String.toDate() : Date {
    val formats = listOf(
        SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US),
        SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
    )

    for (format in formats) {
        try {
            return checkNotNull(format.parse(this))
        } catch (_: ParseException) { }
        catch (_: IllegalStateException) { }
    }

    throw ParseException("Unable to parse $this to a date", 0)
}
