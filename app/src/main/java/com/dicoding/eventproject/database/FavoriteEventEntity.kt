package com.dicoding.eventproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEventEntity(
    @PrimaryKey val id: Int,
    val imageLogo: String,
    val name: String,
    val ownerName: String,
    val beginTime: String,
    val remainingQuota: Int,
    val description: String,
    val link: String,
    val isFavorite: Boolean = true
)
