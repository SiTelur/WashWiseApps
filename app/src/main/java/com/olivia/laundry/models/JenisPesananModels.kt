package com.olivia.laundry.models

import com.google.firebase.firestore.DocumentId

data class JenisPesananModels(
    val ETA:String? = null,
    val HargaPerKilo:Int? = null,
    val Jenis:String? = null,
    @DocumentId val id:String? = null
)