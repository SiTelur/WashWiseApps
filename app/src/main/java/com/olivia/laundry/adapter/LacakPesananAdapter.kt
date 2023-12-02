package com.olivia.laundry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.olivia.laundry.databinding.ListItemLacakPesananBinding
import com.olivia.laundry.models.ProgressModels
import java.text.SimpleDateFormat
import java.util.Date

class LacakPesananAdapter(option:FirestoreRecyclerOptions<ProgressModels>):
    FirestoreRecyclerAdapter<ProgressModels, LacakPesananAdapter.ViewHolder>(option) {
    class ViewHolder(val binding: ListItemLacakPesananBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemLacakPesananBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ProgressModels) {
        with(holder){
            binding.textDate.text = formatter(model.progressDate!!.toDate())
            binding.textView25.text = model.activity
        }
    }

    fun formatter(date: Date): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return formatter.format(date)
    }
}