package com.example.gamepicker

import android.content.Context
import androidx.lifecycle.LiveData

const val HOST = "rawg-video-games-database.p.rapidapi.com"
const val KEY = "8d444f2b43msh8b039748b504b74p187970jsn125e92a633a8"

class GameRepository (context: Context){

    private var gameDao: GameDao

    private val gamesApi: GameApiService = GamesApi.createApi()

    init {
        val gameRoomDatabase = GameRoomDatabase.getDatabase(context)
        gameDao = gameRoomDatabase!!.gameDao()
    }

    fun getRandomGame() = gamesApi.getRandomGame(HOST, KEY, GenerateGame.getRandom())

    fun getAllGames(): LiveData<List<Game>> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(game: Game) {
        gameDao.insertGame(game)
    }

    suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(game)
    }
}