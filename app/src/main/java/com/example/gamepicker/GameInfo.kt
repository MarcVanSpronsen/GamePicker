package com.example.gamepicker

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_game_info.*
import kotlinx.android.synthetic.main.activity_game_info.toolbar
import kotlinx.android.synthetic.main.content_game_info.*

class GameInfo : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    lateinit var currentGame : Game


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_info)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        currentGame  = intent.getParcelableExtra("game")
        initViews()
        initViewModel()

        fab.setOnClickListener { view ->
            onDeleteClicked()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViews() {
        Picasso.with(this).load(currentGame.ImageUrl).fit().centerInside().into(ivInfoGameImage)
        tvInfoName.text = currentGame.Name
        tvInfoReleaseDate.text = currentGame.releaseDate
        tvInfoUrl.text = currentGame.websiteUrl
        tvInfoRating.text = currentGame.rating
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    private fun onDeleteClicked() {
        viewModel.deleteGame(currentGame)
        onSupportNavigateUp()
    }
}
