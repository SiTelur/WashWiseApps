package com.olivia.laundry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.olivia.laundry.databinding.ActivityDetailOrderBinding


class DetailOrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailOrderBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.textView15.text = intent.extras!!.getString("DocID")
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        val progressQuery =
            db.collection("ListPesanan").document(binding.textView15.text.toString())
                .collection("progress").orderBy("progressDate", Query.Direction.ASCENDING)
        progressQuery.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    Log.d("TAG", "Document Id: " + document.id)
                    binding.rvRiwayatPengiriman.text = document.getString("activity")
                    binding.textView54.text =
                        document.getTimestamp("progressDate")?.toDate().toString()

                }
            }

            val getAlamat = currentUser?.uid?.let { it1 ->
                db.collection("User").document(
                    it1
                )
            }
            getAlamat?.get()?.addOnSuccessListener { address ->
                Log.d("DetailOrderActivity", "onCreate: ${address.getString("address")}")
                binding.textView19.text = address.getString("address")
            }

            val getPesanan = db.collection("ListPesanan").document(binding.textView15.text.toString())
            getPesanan.get().addOnSuccessListener {
                binding.Status.text = it.getString("orderStatus")
                Log.d("DetailOrder", "onCreate: ${it.getString("orderStatus")}")
                binding.button.isEnabled = it.getString("orderStatus").equals("Selesai Dicuci")
            }
            binding.rvRiwayatPengiriman.text
        }

    }
}