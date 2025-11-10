
package com.example.travelgo.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("travelgo_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val TOKEN = stringPreferencesKey("auth_token")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN] }

    suspend fun setToken(token: String?) {
        context.dataStore.edit { prefs ->
            if (token == null) prefs.remove(TOKEN) else prefs[TOKEN] = token
        }
    }

    suspend fun clear() = setToken(null)
}
