package com.olivia.laundry.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.content.ContextCompat.startActivity
import com.olivia.laundry.DetailOrderActivity
import com.olivia.laundry.databinding.ListItemHomeBinding
import com.olivia.laundry.models.PesananModels

class HomeAdapter(options: FirestoreRecyclerOptions<PesananModels>) :
    FirestoreRecyclerAdapter<PesananModels, HomeAdapter.ViewHolder>(options) {
    class ViewHolder(val binding:ListItemHomeBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ListItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: PesananModels) {
        with(holder){
            val collectionReference = FirebaseFirestore.getInstance().collection("JenisPesanan")
            val documentReference = model.orderType?.let { collectionReference.document(it) }
            val task = documentReference?.get()
            task?.addOnSuccessListener { document ->
                // Get the document data
                binding.txtpesanan.text = document.getString("Jenis")
                // Do something with the document data
            }
            binding.textView10.text = model.orderStatus
            binding.cvListItemHome.setOnClickListener {
                Log.d("HomeAdapter", "onBindViewHolder: ${model.DocID}")
                val intent = Intent(it.context, DetailOrderActivity::class.java)
                intent.putExtra("DocID",model.DocID)
                startActivity(it.context,intent,null)
            }
        }


    }


}