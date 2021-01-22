package com.gmail.task_8_weather_mvp.data


import com.google.gson.annotations.SerializedName

class CityResponse : ArrayList<CityResponse.CityDataItem>(){
    data class CityDataItem(
        @SerializedName("country")
        val country: String,
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("local_names")
        val localNames: LocalNames,
        @SerializedName("lon")
        val lon: Double,
        @SerializedName("name")
        val name: String,
        @SerializedName("state")
        val state: String
    ) {
        data class LocalNames(
            @SerializedName("ascii")
            val ascii: String,
            @SerializedName("ru")
            val ru: String,
            @SerializedName("en")
            val en: String,
            @SerializedName("feature_name")
            val featureName: String
        )
    }
}