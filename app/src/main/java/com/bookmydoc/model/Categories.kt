package com.bookmydoc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Categories(
    val id: Int=0,
    val name: String="",
    val image: String=""
) : Parcelable
