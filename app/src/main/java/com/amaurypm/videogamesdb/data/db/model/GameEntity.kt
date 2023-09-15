package com.amaurypm.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amaurypm.videogamesdb.util.Constants
import com.amaurypm.videogamesdb.util.DevsList

/**
 * Creado por Amaury Perea Matsumura el 26/08/23
 */

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    val id: Long = 0,

    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "game_genre")
    var genre: String,

    //@ColumnInfo(name = "dev_image")
    //var devImage: DevsList,

    @ColumnInfo(name = "game_developer")
    var developer: String
)
