package com.example.counterboard.utils

import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException


//Everything is in the same file because is an one time resource

data class Anime(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    //@SerializedName("points") val points: String,
)
interface AnimeService {

    @GET("anime?page=1&size=5&sortBy=ranking&sortOrder=asc")
    suspend fun getAnimes(): List<Anime>

    companion object{
        fun getInstance(): AnimeService{

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("x-rapidapi-host", "anime-db.p.rapidapi.com")
                        .build()
                    return chain.proceed(request)
                }
            })
            val client = httpClient.build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://anime-db.p.rapidapi.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(AnimeService::class.java)
        }
    }
}
