package com.example.gamepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.*

const val GENRATE_GAME_REQUEST_CODE = 100
const val DELETE_GAME_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private var games = mutableListOf<Game>()
    private var filteredGames = games
    private val gameAdapter = GameAdapter(games, this::onItemViewClicked, this::onRemoveButtonClicked)
    private val sortMenu = SortMenu(games, gameAdapter)
    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViews()
        InitViewModel()

        generate.setOnClickListener { view ->
            startGenerateActivity()
        }
    }

    private fun startGenerateActivity() {
        val intent = Intent(this, GenerateGame::class.java)
        startActivityForResult(intent, GENRATE_GAME_REQUEST_CODE)
    }

    private fun startInfoActivity(game: Game) {
        val intent = Intent(this, GameInfo::class.java)
        intent.putExtra("game", game)
        startActivityForResult(intent, DELETE_GAME_REQUEST_CODE)
    }

    private fun initViews() {
        setListeners()
        createItemTouchHelper().attachToRecyclerView(rvGames)
        rvGames.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvGames.adapter = gameAdapter

        // Set searchView textColor
        val id =
            svSearch.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = svSearch.findViewById(id) as TextView
        textView.setTextColor(Color.WHITE)

        setListeners()
    }

    private fun InitViewModel() {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.games.observe(this, Observer { games ->
            this@MainActivity.games.clear()
            this@MainActivity.games.addAll(games)
            gameAdapter.notifyDataSetChanged()
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setListeners() {
        sortLayout.setOnClickListener { onSortMenuOpened() }

        ibtnSearch.setOnClickListener { onSearchBarOpened() }

        svSearch.setOnCloseListener { onSearchBarClosed(); false }

        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText); return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query); return false
            }

        })

        imgbtnCloseSearch.setOnClickListener { onSearchBarClosed() }
    }

    private fun createItemTouchHelper() : ItemTouchHelper {
        val callback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = filteredGames[position]

                viewModel.deleteGame(gameToDelete)
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun onItemViewClicked(game: Game) {
        startInfoActivity(game)
    }

    private fun onRemoveButtonClicked(game: Game) {
        viewModel.deleteGame(game)
    }

    /**
     * Called when the sort button is clicked.
     *
     */
    private fun onSortMenuOpened() {
        sortMenu.openMenu(this, btnSort, filteredGames)
    }

    /**
     * Method to show the search bar and hide the toolbar.
     *
     */
    private fun onSearchBarOpened() {
        svSearch?.setQuery("", true)
        llSearch?.visibility = View.VISIBLE
        svSearch?.isIconified = false
        toolbar?.visibility = View.GONE
    }

    /**
     * Method to hide the search bar and show the toolbar.
     *
     */
    private fun onSearchBarClosed() {
        svSearch?.setQuery("", true)
        llSearch?.visibility = View.GONE
        toolbar?.visibility = View.VISIBLE
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            when (requestCode) {
                GENRATE_GAME_REQUEST_CODE -> {
                    val game = data!!.getParcelableExtra<Game>(EXTRA_GAME)
                    viewModel.insertGame(game)
                }
            }
        }
    }

    /**
     * Method that is called when text is entered in search view.
     *
     * @param query entered string used as search query.
     */
    private fun filter(query: String) {
        filteredGames = games.filter { product ->
            product.Name.toLowerCase().contains(query.toLowerCase())
        } as MutableList<Game>

        gameAdapter.games = filteredGames
        gameAdapter.notifyDataSetChanged()
        sortMenu.sortFilteredList(filteredGames)

        onNoResults()
    }

    /**
     * Called after filtering products array to show or hide no results text view.
     *
     */
    private fun onNoResults() {
        if (gameAdapter.itemCount == 0) {
            tvNoResults.visibility = View.VISIBLE
        } else {
            tvNoResults.visibility = View.INVISIBLE
        }
    }
}
