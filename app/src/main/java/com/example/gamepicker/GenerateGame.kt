package com.example.gamepicker

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_generate_game.*
import kotlinx.android.synthetic.main.content_generate_game.*
import org.joda.time.DateTime
import java.util.concurrent.Executors
import kotlin.random.Random

const val EXTRA_GAME = "EXTRA_GAME"

class GenerateGame : AppCompatActivity() {

    private lateinit var viewModel: ViewModel

    companion object {
        fun getRandom(): String {
            var random = Random.nextInt(1, 30000).toString()
            return random
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_game)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        InitViewModel(this)


        generate.setOnClickListener { view ->
            onGenerateClick(this)
        }

        add.setOnClickListener { view ->
            onSaveClick()
        }
    }

    private fun InitViewModel(context: Context) {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.getRandomGame()
        viewModel.game.observe(this, Observer {
            Picasso.with(context).load(it.ImageUrl).fit().centerInside().into(ivGameImage)
            tvImageUrl.text = it.ImageUrl
            tvName.text = it.Name
            tvReleaseDate.text = it.releaseDate
            tvUrl.text = it.websiteUrl
            tvRating.text = it.rating
            CheckEmptyFields(it)
        })
        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onGenerateClick(context: Context) {
        viewModel.getRandomGame()
        viewModel.game.observe(this, Observer {
            Picasso.with(context).load(it.ImageUrl).fit().centerInside().into(ivGameImage)
            tvImageUrl.text = it.ImageUrl
            tvName.text = it.Name
            tvReleaseDate.text = it.releaseDate
            tvUrl.text = it.websiteUrl
            tvRating.text = it.rating
            CheckEmptyFields(it)
        })
        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun onSaveClick() {
        val game = Game(
            null,
            tvName.text.toString(),
            tvReleaseDate.text.toString(),
            tvUrl.text.toString(),
            tvImageUrl.text.toString(),
            tvRating.text.toString(),
            tvReleaseDate.text.toString()
        )
        val resultIntent = intent
        resultIntent.putExtra(EXTRA_GAME, game)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun CheckEmptyFields(game: Game) {
        if (game.ImageUrl.isNullOrBlank()) {
            ivGameImage.setImageDrawable(getDrawable(R.drawable.ic_launcher))
        }
        if (game.Name.isNullOrBlank()) {
            tvName.text = "Unknown Name"
        }
        if (game.releaseDate.isNullOrBlank()) {
            tvReleaseDate.text = "No release date listed"
        }
        if (game.websiteUrl.isNullOrBlank()) {
            tvUrl.text = "No webstite URL listed"
        }
        if (game.rating.isNullOrBlank()) {
            tvRating.text = "No Rating found"
        }
    }
}
