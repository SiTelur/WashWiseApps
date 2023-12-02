package com.olivia.laundry

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.olivia.laundry.databinding.ActivityKalkulatorBinding


class KalkulatorActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityKalkulatorBinding
    val subjects: MutableList<String?> = ArrayList()
    var harga:Double= 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKalkulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootRef = FirebaseFirestore.getInstance()
        val subjectsRef = rootRef.collection("JenisPesanan")

        val adapter = ArrayAdapter(applicationContext, R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerJenis.adapter = adapter
        subjectsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val subject = document.getString("jenis")
                    subjects.add(subject)
                }
                adapter.notifyDataSetChanged()
            }
        }
        binding.spinnerJenis.onItemSelectedListener = this

        binding.Status.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.Status.length() != 0) {
                    binding.txtPassword.setText("");
                }

            }
            override fun afterTextChanged(p0: Editable?) {
                if ((binding.Status.text).toString() == ""){
                    binding.txtPassword.text = p0
                }else{
                    binding.txtPassword.setText((binding.Status.text.toString().toDouble() * harga).toString())
                }
            }
        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d("Kalkulator", "onItemSelected: ${subjects[p2]}")
        val db = FirebaseFirestore.getInstance()
        val cari = db.collection("JenisPesanan").whereEqualTo("jenis", subjects[p2])
        cari.get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        Log.d("Kalkulator", document.get("hargaPerKilo").toString());
                        harga = document.getDouble("hargaPerKilo")!!
                    }
                } else {
                    Log.e("Kalkulator", "onItemSelected: gagal", it.exception)
                }
            }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d("Kalkulator", "onNothingSelected: not selectedYet")
    }
}