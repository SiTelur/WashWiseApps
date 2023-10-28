package com.olivia.laundry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.olivia.laundry.databinding.ListItemPesananBinding
import com.olivia.laundry.models.JenisPesananModels


class PesananAdapter(option:FirestoreRecyclerOptions<JenisPesananModels>): FirestoreRecyclerAdapter<JenisPesananModels, PesananAdapter.ViewHolder>(option) {
    private val selectCheck = ArrayList<Int>()
    var supportSchoolIdModels: ArrayList<JenisPesananModels>? = null
    var ischecked: Boolean = false
    var listener: CheckboxListener? = null

    inner class ViewHolder(val binding: ListItemPesananBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemPesananBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    private var selectedPosition = -1 // no selection by default
    var selections = mutableSetOf<String>()
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: JenisPesananModels) {
        with(holder){

            binding.eta.text = model.ETA
            binding.jenis3.text = model.HargaPerKilo.toString()
            binding.jenis.text = model.Jenis

            val modelItem = getItem(position)

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (selections.size < 1) {
                        modelItem.Jenis?.let { selections.add(it) }
                        listener?.onCheckboxClicked(modelItem.HargaPerKilo)
                    } else {
                        binding.checkBox.isChecked = false
                        // Show snackbar that max selections reached
                    }
                } else {
                    selections.remove(modelItem.Jenis)
                    listener?.onCheckboxClicked(0)

                }

        }
    }
}

    interface CheckboxListener {
        fun onCheckboxClicked(model: Int?)
    }
}