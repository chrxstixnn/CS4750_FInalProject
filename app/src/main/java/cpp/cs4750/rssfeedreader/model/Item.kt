package cpp.cs4750.rssfeedreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Item(
    val title: String?,
    val author: String?,
    val description: String?,
    val link: String?,
    val pubDate: String?,
    @PrimaryKey val id: UUID = UUID.randomUUID()
) {
    override fun equals(other: Any?): Boolean = (other as? Item)?.let {
        this.title.equals(it.title) &&
        this.author.equals(it.author) &&
        this.pubDate.equals(it.pubDate)
    } ?: false
}
