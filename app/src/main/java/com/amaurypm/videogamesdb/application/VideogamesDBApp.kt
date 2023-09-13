package com.amaurypm.videogamesdb.application

import android.app.Application
import com.amaurypm.videogamesdb.data.GameRepository
import com.amaurypm.videogamesdb.data.db.GameDatabase

/**
 * Creado por Amaury Perea Matsumura el 26/08/23
 */


class VideogamesDBApp(): Application() {
    private val database by lazy{
        GameDatabase.getDatabase(this@VideogamesDBApp)
    }

    val repository by lazy{
        GameRepository(database.gameDao())
    }
}