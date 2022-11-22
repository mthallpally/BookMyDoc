package com.bookmydoc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    var booking_id: String = "",
    var doctor_id: String = "",
    var user_id: String = "",
    val name: String = "",
    val topic: String = "",
    val time: String = "",
    val date: String = "",
) : Parcelable
