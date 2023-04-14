package com.poc.firstprojectinfnet.home.data.datasource

import com.poc.firstprojectinfnet.home.data.TaskLocate
import com.poc.firstprojectinfnet.home.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class WeatherDataSource {

    suspend fun getWeatherData(lat: Double?, long: Double?): WeatherData {
        val apiKey = "91fbe21a8c3c4d3d8ef174846232703"
        val url =
            "https://api.weatherapi.com/v1/current.json?lang=pt&key=$apiKey&q=${lat},${long}"
        val jsonText = withContext(Dispatchers.IO) { URL(url).readText() }
        val jsonObject = JSONObject(jsonText)
        val current = jsonObject.getJSONObject("current")
        val temperature = current.getDouble("temp_c")
        val description = current.getJSONObject("condition").getString("text")
        val conditionCode = current.getJSONObject("condition").getInt("code")
        return WeatherData(temperature, description, conditionCode)
    }
}