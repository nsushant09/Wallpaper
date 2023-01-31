package com.neupanesushant.wallpaper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageSrc(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
) : Parcelable