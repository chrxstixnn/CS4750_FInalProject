package cpp.cs4750.rssfeedreader.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cpp.cs4750.rssfeedreader.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getItems(): Flow<List<Item>>

    @Update
    suspend fun updateItem(item: Item)

    @Insert
    suspend fun addItem(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItems(items: List<Item>)
}