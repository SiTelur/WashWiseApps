package com.olivia.laundry.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class PesananModels(
    var orderStatus: String? = null,
    var UID:String? = null,
    @ServerTimestamp val orderDate: Timestamp? = null,
    var orderType:String? = null,
    var qty:Int? = null,
    @DocumentId var DocID:String?= null,
    var paymentStatus:String? = null,
    var orderTotal:Int? = null,
    var useVoucher:Boolean? = false
)