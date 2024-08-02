package com.example.lesson19_2

import org.osmdroid.util.GeoPoint

data class LandmarkClass(
    val point: GeoPoint,
    val name: String,
    val description: String
)