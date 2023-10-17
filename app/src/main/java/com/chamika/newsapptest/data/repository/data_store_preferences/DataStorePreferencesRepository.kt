package com.chamika.newsapptest.data.repository.data_store_preferences

interface DataStorePreferencesRepository {
        suspend fun putString(key:String,value:String)
        suspend fun putBoolean(key:String,value:Boolean)
        suspend fun putInt(key:String,value:Int)
        suspend fun getString(key: String):String?
        suspend fun getBoolean(key: String):Boolean?
        suspend fun getInt(key: String):Int
        suspend fun clearPReferences(key: String)
}