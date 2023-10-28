package com.olivia.laundry.models

import com.google.firebase.firestore.GeoPoint

data class AlamatModels(
    var address:String? = null,
    var coordinatePoint:GeoPoint? = null
)
