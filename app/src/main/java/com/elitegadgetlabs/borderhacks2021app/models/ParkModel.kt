package com.elitegadgetlabs.borderhacks2021app.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParkModel(
    val parks: List<Park>
)

@JsonClass(generateAdapter = true)
data class Park(
    val name: String,
    val address: String,
    val description: String,
    val tags: List<String>,
    val location: List<Double>,
    val img_url: String
)