package com.bloom.musicbridge

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/update")
    fun sendSongInfo(@Body songInfo: SongInfo): Call<Void>
}