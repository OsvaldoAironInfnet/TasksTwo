package com.poc.commom.base.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class BaseRepository<T> {

    val gson by lazy {
        Gson()
    }

    fun convertAnyToObject(data: Any): T {
        return gson.fromJson(gson.toJson(data), object : TypeToken<T>() {}.type)
    }
}