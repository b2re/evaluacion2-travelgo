
package com.example.travelgo.data.remote

import com.example.travelgo.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val session: SessionManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        // Skip if already has Authorization
        if (original.header("Authorization") != null) return chain.proceed(original)

        val token = runBlocking { session.tokenFlow.first() }
        val req = if (token.isNullOrBlank()) original else {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
        return chain.proceed(req)
    }
}
