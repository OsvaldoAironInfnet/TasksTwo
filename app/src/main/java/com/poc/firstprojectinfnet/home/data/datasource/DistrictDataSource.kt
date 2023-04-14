package com.poc.firstprojectinfnet.home.data.datasource

import com.poc.firstprojectinfnet.home.data.DistrictDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class DistrictDataSource {
     suspend fun getCityDistrict(latitude: Double, longitude: Double): DistrictDate {
        val url =
            "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=$latitude&longitude=$longitude&localityLanguage=pt"
        val jsonText = withContext(Dispatchers.IO) { URL(url).readText() }
        val jsonObject = JSONObject(jsonText)
        val city = jsonObject.getString("city")
        val district = jsonObject.getString("locality")
        return DistrictDate(city, district)
    }
}