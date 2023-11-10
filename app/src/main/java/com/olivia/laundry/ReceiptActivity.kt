package com.olivia.laundry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.olivia.laundry.databinding.ActivityReceiptBinding

class ReceiptActivity : AppCompatActivity() {
    lateinit var binding:ActivityReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()


        binding.textView29.text = intent.extras!!.getString("orderID")
        binding.textView30.text = intent.extras!!.getString("orderName")
        binding.textView33.text = intent.extras!!.getString("tanggalOrderengge")
        binding.textView68.text = intent.extras!!.getString("serviceSatuan")
        binding.textView26.text = intent.extras!!.getString("banyak")
        binding.textView69.text = intent.extras!!.getString("hasilPerkalian")
        binding.textView40.text = intent.extras!!.getString("total")

        binding.textView38.text = binding.textView69.text


        val getPaymentDetail = db.collection("ListPesanan").document(binding.textView29.text.toString()).collection("Payment").document("detailPayment")
        getPaymentDetail.get()
            .addOnSuccessListener {
               binding.textView36.text =  it.getString("method")
               binding.textView23.text =  it.getString("transactionID")
            }
    }
}