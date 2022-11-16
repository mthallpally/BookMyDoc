package com.bookmydoc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id : String = "",
    val fullName : String = "",
    val email : String = "",
    val mobile : Long = 0,
    val countryCode : String = "",
    val image : String = "",
    val gender : String = "",
    val profileCompleted : Int = 0

        ) : Parcelable