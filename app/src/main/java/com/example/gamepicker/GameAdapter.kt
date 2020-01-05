package com.example.gamepicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_generate_game.view.*
import kotlinx.android.synthetic.main.item_game.view.*

class GameAdapter (initialGames: List<Game>, private val itemClickListener: (Game) -> Unit, private val removeClickListener: (Game) -> Unit) :
RecyclerView.Adapter<GameAdapter.ViewHolder>(){

    /**
     * Context of the activity.
     */
    lateinit var context: Context
    /**
     * List of products in the recycler view.
     */
    var games: List<Game> = initialGames

    /**
     * Creates a new ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return Returns the new ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        )
    }

    /**
     * Gets the amount of items in the ListView.
     *
     * @return The amount of items.
     */
    override fun getItemCount(): Int {
        return games.size
    }

    /**
     * Updates the contents of the itemView to reflect the item at the given position.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item to be updated.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position], itemClickListener, removeClickListener)
    }

    /**
     * Represents an element in the RecyclerView.
     *
     * @param itemView The xml file that represents an item.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Sets the values of the itemView to the product values.
         *
         * @param game The game that is bound to the itemView.
         */
        fun bind(game: Game, itemClickListener: (Game) -> Unit, removeClickListener: (Game) -> Unit) {
            itemView.gameID.text = game.uid.toString()

            // LayoutParams for setting margins programmatically
            val lp = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            // Last product in list, remove decorator and add extra bottom-margin
            if(games[games.lastIndex] == game) {
                itemView.borderDecorator.visibility = View.INVISIBLE
                val unbounded = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                itemView.measure(unbounded, unbounded)
                val bottomMargin = itemView.measuredHeight
                lp.setMargins(0, 0, 0, bottomMargin)
            } else {
                itemView.borderDecorator.visibility = View.VISIBLE
                lp.setMargins(0, 0, 0, 0)
            }
            itemView.layoutParams = lp

            // Set values in layout
            itemView.tvGameName.text = game.Name
            itemView.tvDate.text = game.releaseDate
            itemView.btnRemove.setOnClickListener{ removeClickListener(game) }
            itemView.setOnClickListener{ itemClickListener(game) }
        }
    }
}