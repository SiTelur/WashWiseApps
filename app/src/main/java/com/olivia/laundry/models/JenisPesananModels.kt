package com.olivia.laundry.models

import com.google.firebase.firestore.DocumentId

data class JenisPesananModels(
    val eta:String? = null,
    val hargaPerKilo:Int? = null,
    val jenis:String? = null,
    @DocumentId val id:String? = null
)