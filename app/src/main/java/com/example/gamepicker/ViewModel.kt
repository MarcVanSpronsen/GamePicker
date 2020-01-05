package com.example.gamepicker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel (application: Application) :
    AndroidViewModel(application) {

    private val gameRepository = GameRepository(application.applicationContext)
    private val ioScope = CoroutineScope(Dispatchers.IO)
    val game = MutableLiveData<Game>()
    val error = MutableLiveData<String>()

    val games = gameRepository.getAllGames()

    fun insertGame(game: Game) {
        ioScope.launch {
            gameRepository.insertGame(game)
        }
    }

    fun deleteGame(game: Game) {
        ioScope.launch {
            gameRepository.deleteGame(game)
        }
    }

    fun getRandomGame() {
        gameRepository.getRandomGame().enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) game.value = response.body()
                else error.value = "An error occurred: ${response.errorBody().toString()}"
                GenerateGame
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                error.value = t.message
            }
        })
    }
}