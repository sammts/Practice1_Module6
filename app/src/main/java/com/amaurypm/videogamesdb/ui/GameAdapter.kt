package com.amaurypm.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.convertTo
import androidx.recyclerview.widget.RecyclerView
import com.amaurypm.videogamesdb.R
import com.amaurypm.videogamesdb.data.db.model.GameEntity
import com.amaurypm.videogamesdb.databinding.ActivityMainBinding
import com.amaurypm.videogamesdb.databinding.GameElementBinding

/**
 * Creado por Amaury Perea Matsumura el 26/08/23
 */
class GameAdapter(private val onGameClick: (GameEntity) -> Unit): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var games: List<GameEntity> = emptyList()

    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivIcon = binding.ivIcon

        fun bind(game: GameEntity){
            /*binding.tvTitle.text = game.title
            binding.tvGenre.text = game.genre
            binding.tvDeveloper.text = game.developer*/

            binding.apply {
                tvTitle.text = game.title
                tvGenre.text = game.genre
                if (game.devImage == 0) {
                    tvDeveloper.text = "Activision Blizzard"
                } else if (game.devImage == 1) {
                    tvDeveloper.text = "Sony Iteractive Entertainment"
                } else if (game.devImage == 2) {
                    tvDeveloper.text = "Nintendo"
                } else if (game.devImage == 3) {
                    tvDeveloper.text = "Microsoft"
                } else if (game.devImage == 4) {
                    tvDeveloper.text = "Tencent"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        holder.bind(game)

        if (game.devImage == 0)  {
            holder.ivIcon.setImageResource(R.drawable.activision)
        } else if (game.devImage == 1)  {
            holder.ivIcon.setImageResource(R.drawable.sony)
        } else if (game.devImage == 2)  {
            holder.ivIcon.setImageResource(R.drawable.nintendo)
        } else if (game.devImage == 3)  {
            holder.ivIcon.setImageResource(R.drawable.microsoft)
        } else if (game.devImage == 4)  {
            holder.ivIcon.setImageResource(R.drawable.tencent)
        }

        holder.itemView.setOnClickListener {
            //Aquí va el click del elemento
            onGameClick(games[position])
        }

        holder.ivIcon.setOnClickListener {
            //Click para la vista del imageview con el ícono
        }

    }

    fun updateList(list: List<GameEntity>){
        games = list
        notifyDataSetChanged()
    }

}