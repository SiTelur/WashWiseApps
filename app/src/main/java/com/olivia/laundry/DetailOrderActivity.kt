package com.olivia.laundry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.olivia.laundry.databinding.ActivityDetailOrderBinding
import com.olivia.laundry.fragment.LacakPesananFragment
import java.text.SimpleDateFormat
import java.util.Date


class DetailOrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailOrderBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var namaService:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar3.visibility = View.VISIBLE;
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        var qty =0.0
        auth = FirebaseAuth.getInstance()

        binding.textView15.text = intent.extras!!.getString("DocID")
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        val progressQuery =
            db.collection("ListPesanan").document(binding.textView15.text.toString())
                .collection("progress").orderBy("progressDate", Query.Direction.ASCENDING)
        progressQuery.get().addOnCompleteListener { it ->
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
                binding.textView62.text = "${it.get("qty").toString()}"
                qty = it.getDouble("qty")!!
                Log.d("DetailOrder", "onCreate: ${it.getString("orderStatus")}")

                val getJenisPesanan =
                    it.getString("orderType")
                        ?.let { it1 -> db.collection("JenisPesanan").document(it1) }
                getJenisPesanan?.get()?.addOnSuccessListener { jenisPesanan ->
                    binding.textView63.text = jenisPesanan.get("HargaPerKilo").toString()
                    namaService = jenisPesanan.getString("Jenis").toString()
                    if (binding.textView62.text == "0"){
                        binding.textView64.text = "Berat Belum Dihitung"
                    }else{
                        binding.textView64.text =
                            (it.get("qty").toString().toBigDecimalOrNull()?.let { it1 ->
                                binding.textView63.text.toString().toBigDecimalOrNull()
                                    ?.times(it1)
                            }).toString()

                        binding.textView23.text = binding.textView64.text.toString()
                    }
                    binding.progressBar3.visibility = View.GONE;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                binding.button.isEnabled = it.getString("orderStatus").equals("Pesanan Siap Dikirim")
            }
            binding.rvRiwayatPengiriman.text


        }

        binding.button.setOnClickListener{
            val intent = Intent(this,CheckOutActivity::class.java)
            intent.putExtra("DocID",binding.textView15.text)
            intent.putExtra("ServiceSatuan",binding.textView63.text)
            intent.putExtra("BanyakService",binding.textView62.text)
            intent.putExtra("TotalService",binding.textView23.text)
            intent.putExtra("jumlahHargaService",binding.textView64.text)
            intent.putExtra("UID",auth.currentUser!!.uid)
            intent.putExtra("NamaService",namaService)
            startActivity(intent)
        }

        binding.textView17.setOnClickListener {
            (binding.textView15.text as String?)?.let { it1 -> LacakPesananFragment.newInstance(it1,null).show(supportFragmentManager,"LacakPesanan") }
        }


    }

//    override fun onStart() {
//        super.onStart()
//        val db = FirebaseFirestore.getInstance()
//        db.collection("ListPesanan").document(binding.textView15.text.toString()).get().addOnSuccessListener {
//            val getJenisPesanan =
//                it.getString("orderType")
//                    ?.let { it1 -> db.collection("JenisPesanan").document(it1) }
//            getJenisPesanan?.get()?.addOnSuccessListener { jenisPesanan ->
//            if (it.getString("paymentStatus").toString() == "Success") {
//                val intent = Intent(this, ReceiptActivity::class.java)
//                intent.putExtra("orderID", binding.textView15.text)
//                intent.putExtra("orderName", jenisPesanan.getString("Jenis").toString())
//                intent.putExtra("serviceSatuan", binding.textView63.text)
//                intent.putExtra("banyak", binding.textView62.text)
//                intent.putExtra("total", binding.textView23.text)
//                intent.putExtra("hasilPerkalian", binding.textView64.text)
//                intent.putExtra("tanggalOrder", formatter(it.getDate("orderDate")))
//                startActivity(intent)
//                finish()
//            }
//            }
//        }
//    }
    fun formatter(date: Date?): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return formatter.format(date)
    }

}