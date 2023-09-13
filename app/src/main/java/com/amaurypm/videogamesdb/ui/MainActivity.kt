package com.amaurypm.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amaurypm.videogamesdb.R
import com.amaurypm.videogamesdb.application.VideogamesDBApp
import com.amaurypm.videogamesdb.data.GameRepository
import com.amaurypm.videogamesdb.data.db.model.GameEntity
import com.amaurypm.videogamesdb.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var games: List<GameEntity> = emptyList()
    private lateinit var repository: GameRepository

    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideogamesDBApp).repository

        gameAdapter = GameAdapter(){ game ->
            gameClicked(game)
        }

        //binding.rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
        //binding.rvGames.adapter = gameAdapter

        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }

        updateUI()

    }

    private fun updateUI(){
        lifecycleScope.launch {
            games = repository.getAllGames()

            if(games.isNotEmpty()){
                //Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
            }else{
                //No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            gameAdapter.updateList(games)
        }
    }

    fun click(view: View) {
        val dialog = GameDialog( updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun gameClicked(game: GameEntity){
        //Toast.makeText(this, "Click en el juego con id: ${game.id}", Toast.LENGTH_SHORT).show()
        val dialog = GameDialog(newGame = false, game = game, updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
}