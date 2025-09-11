package com.bloom.musicbridge

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SongSender {
    fun sendSong(songInfo: SongInfo) {
        val service = RetrofitClient.apiService ?: return
        service.sendSongInfo(songInfo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("SongSender", "Sent song: ${songInfo.title} - ${songInfo.artist}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("SongSender", "Failed to send song", t)
            }
        })
    }
}
