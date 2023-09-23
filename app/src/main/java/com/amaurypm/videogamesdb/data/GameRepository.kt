package com.amaurypm.videogamesdb.data

import android.media.Image
import com.amaurypm.videogamesdb.data.db.GameDao
import com.amaurypm.videogamesdb.data.db.model.GameEntity
import com.amaurypm.videogamesdb.util.DevsList

/**
 * Creado por Amaury Perea Matsumura el 26/08/23
 */
class GameRepository(private val gameDao: GameDao) {

    suspend fun insertGame(game: GameEntity){
        gameDao.insertGame(game)
    }

    suspend fun insertGame(title: String, genre: String, developer: String, devImage: Int){
        gameDao.insertGame(GameEntity(title = title, genre = genre, developer = developer, devImage = devImage))
    }

    suspend fun getAllGames(): List<GameEntity> = gameDao.getAllGames()

    suspend fun updateGame(game: GameEntity){
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(game: GameEntity){
        gameDao.deleteGame(game)
    }


}