package com.olivia.laundry.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ProgressModels(
    var progressDate:Timestamp? = null,
    var activity:String? = null,
    @DocumentId var UID:String? = null
)
