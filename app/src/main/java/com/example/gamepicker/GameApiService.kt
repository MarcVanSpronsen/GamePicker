package com.example.gamepicker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.random.Random

interface GameApiService {

    @GET("/games/{game}")
    fun getRandomGame(
        @Header("x-rapidapi-host") xRapidApiHost: String,
        @Header("x-rapidapi-key") XRapidApiKey: String,
        @Path("game") gameID : String
    ): Call<Game>
}