package com.maulnad.academy.data.source.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentResponse(
    var moduleId: String,
    var content: String
) : Parcelable