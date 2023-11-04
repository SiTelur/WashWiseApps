package com.olivia.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.olivia.laundry.R
import com.olivia.laundry.databinding.RiwayatPemesananListBinding
import com.olivia.laundry.models.PesananModels
import java.text.SimpleDateFormat
import java.util.Date

class RiwayatAdapter(option: FirestoreRecyclerOptions<PesananModels>):
    FirestoreRecyclerAdapter<PesananModels, RiwayatAdapter.ViewHolder>(option) {
   inner class ViewHolder(val binding: RiwayatPemesananListBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RiwayatPemesananListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: PesananModels) {

        with(holder){
            val collectionReference = FirebaseFirestore.getInstance().collection("JenisPesanan")
            val documentReference = model.orderType?.let { collectionReference.document(it) }
            val task = documentReference?.get()
            task?.addOnSuccessListener { document ->
                // Get the document data
                binding.textView56.text = document.getString("Jenis")
                // Do something with the document data
            }

            val getPesanan =
                model.DocID?.let {
                    FirebaseFirestore.getInstance().collection("ListPesanan").document(
                        it
                    )
                }
            getPesanan?.get()?.addOnSuccessListener {
                binding.textView58.text = it.getString("orderStatus")
              binding.textView57.text = it.getDate("orderDate")?.let { it1 -> formatter(it1) }
            }
        }
    }

    fun formatter(date: Date): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return formatter.format(date)
    }


}