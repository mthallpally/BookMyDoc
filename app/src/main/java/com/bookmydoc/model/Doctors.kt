package com.bookmydoc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctors(
    var doctor_id: String = "",
    val name: String = "",
    val categories: String = "",
    val categories_id: Int = 0,
    val patient: String = "",
    val experience: String = "",
    val rating: Double = 0.0,
    val mobile_number: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val topic: ArrayList<String> = ArrayList<String>()
) : Parcelable
