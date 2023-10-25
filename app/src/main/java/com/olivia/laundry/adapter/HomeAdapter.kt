package com.olivia.laundry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.databinding.ListItemHomeBinding
import com.olivia.laundry.models.PesananModels

class HomeAdapter(private val options: FirestoreRecyclerOptions<PesananModels>) :
    FirestoreRecyclerAdapter<PesananModels, HomeAdapter.ViewHolder>(options) {
    class ViewHolder(val binding:ListItemHomeBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ListItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: PesananModels) {
        with(holder){
            val collectionReference = FirebaseFirestore.getInstance().collection("JenisPesanan")
            val documentReference = model.JenisPesanan?.let { collectionReference.document(it) }
            val task = documentReference?.get()
            task?.addOnSuccessListener { document ->
                // Get the document data
                binding.txtpesanan.text = document.getString("Jenis")
                // Do something with the document data
            }
            binding.textView10.text = model.StatusPesanan
        }
    }


}