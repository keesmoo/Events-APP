package com.dicoding.eventproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteEvent(event: FavoriteEventEntity)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Query("DELETE FROM favorite_events WHERE id = :eventId")
    suspend fun removeFavoriteEvent(eventId: Int)

    @Query("SELECT * FROM favorite_events WHERE id = :eventId LIMIT 1")
    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEventEntity?>
}
