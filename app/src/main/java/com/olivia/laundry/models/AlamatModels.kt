package com.olivia.laundry.models

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize


data class AlamatModels(
    var address:String? = null,
    var coordinatePoint:GeoPoint? = null
)
