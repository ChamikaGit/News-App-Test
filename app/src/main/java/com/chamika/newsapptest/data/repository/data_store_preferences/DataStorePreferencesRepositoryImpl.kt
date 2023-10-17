package com.chamika.newsapptest.data.repository.data_store_preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStorePreferencesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    DataStorePreferencesRepository {


    override suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferenceKey = intPreferencesKey(key)
        dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferenceKey = stringPreferencesKey(key)
            val preference = dataStore.data.first()
            preference[preferenceKey] ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return try {
            val preferenceKey = booleanPreferencesKey(key)
            val preference = dataStore.data.first()
            preference[preferenceKey] ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getInt(key: String): Int {
        return try {
            val preferenceKey = intPreferencesKey(key)
            val preference = dataStore.data.first()
            preference[preferenceKey] ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun clearPReferences(key: String) {
        val preferenceKey = stringPreferencesKey(key)
        dataStore.edit {
            if (it.contains(preferenceKey)) {
                it.remove(preferenceKey)
            }
        }
    }

}