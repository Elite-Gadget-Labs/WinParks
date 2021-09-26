package com.elitegadgetlabs.borderhacks2021app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitegadgetlabs.borderhacks2021app.models.Filter
import com.elitegadgetlabs.borderhacks2021app.models.ParkModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap

import com.google.maps.android.data.geojson.GeoJsonLayer

import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

class MainViewModel: ViewModel() {

    private val client = OkHttpClient()


    fun httpRequest(url: String): Response? {

        val request = Request.Builder()
            .url(url)
            .build()

        var response: Response?

//        lateinit var result: String
        runBlocking(Dispatchers.IO) {
            response = client.newCall(request).execute()
//            result = response?.body?.string().toString()
        }

        return response
    }

    fun getParkData(): ParkModel? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: com.squareup.moshi.JsonAdapter<ParkModel> =
            moshi.adapter(ParkModel::class.java)

        var parks: ParkModel?
        runBlocking(Dispatchers.IO) {
            parks = jsonAdapter.fromJson(
                httpRequest("https://borderhacks2021-default-rtdb.firebaseio.com/parkdata.json")?.body?.source()!!
            )
        }

        return parks
    }

    fun getParkGeoData(googleMap:GoogleMap): GeoJsonLayer? {
        val geoJson = httpRequest("https://borderhacks2021-default-rtdb.firebaseio.com/parkgeojson.json")?.body!!

        val layer: GeoJsonLayer?
        runBlocking(Dispatchers.IO) {
            layer = GeoJsonLayer(googleMap, JSONObject(geoJson.string()))
        }

        return layer
    }


}