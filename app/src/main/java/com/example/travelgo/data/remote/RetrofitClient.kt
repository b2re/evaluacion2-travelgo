
package com.example.travelgo.data.remote

import android.content.Context
import com.example.travelgo.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // 👇 Base de Xano entregada por el usuario (termina en /)
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW/"

    fun build(context: Context): Retrofit {
        val session = SessionManager(context)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(session))
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
