package com.olivia.laundry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.olivia.laundry.databinding.ActivityCheckOutBinding
import com.olivia.laundry.models.PayModels
import java.text.SimpleDateFormat
import java.util.Date


class CheckOutActivity : AppCompatActivity(), TransactionFinishedCallback {
    lateinit var binding: ActivityCheckOutBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        var selectedPayment = ""
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.textView15.text = intent.extras?.getString("DocID")
        binding.textView67.text = intent.extras?.getString("BanyakService")
        binding.textView60.text = intent.extras?.getString("ServiceSatuan")
        binding.textView36.text = intent.extras?.getString("TotalService")
        binding.textView59.text = intent.extras?.getString("TotalService")
        binding.textView23.text = intent.extras?.getString("TotalService")

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioButton.id -> {
                    selectedPayment = "qris"
                }

                binding.radioButton2.id -> {
                    selectedPayment = "cod"
                }
            }
        }

        binding.button2.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    101
                );
            }

            when (selectedPayment) {
                "qris" -> {
                    clickPay()

                    Log.d("CheckOutActivity", "onCreate: Anda Menggunakan QRIS ")
                }

                "cod" -> {
                    Log.d("CheckOutActivity", "onCreate: Anda Menggunakan COD")
                    val payModels = PayModels("COD", "Anda Menggunakan COD")
                    val db = FirebaseFirestore.getInstance()
                    val tambahPayment = db.collection("ListPesanan")
                        .document(intent.extras?.getString("DocID").toString())
                        .collection("Payment").document("detailPayment")
                    tambahPayment.set(payModels)
                        .addOnSuccessListener {
                            Log.d("Payment", "onTransactionFinished: Berhasil Menyimpan")
                        }.addOnFailureListener {
                            Log.e("Payment", "onTransactionFinished: Gagal Menyimpan", it.cause)
                        }
                    ubahOrder("Success")
                    Toast.makeText(
                        this,
                        "Transaction Sukses",
                        Toast.LENGTH_LONG
                    ).show()
                    openReceiptActivity()
                }
                else -> Toast.makeText(this, "Anda Harus Memilih 1 Pembayaran", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        makePayment()
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    private fun makePayment() {
        SdkUIFlowBuilder.init()
            .setContext(this)
            .setMerchantBaseUrl(BuildConfig.BASE_URL)
            .setClientKey(BuildConfig.CLIENT_KEY)
            .setTransactionFinishedCallback(this)
            .enableLog(true)
            .setLanguage("id")
            .setColorTheme(CustomColorTheme("#1C4A86", "#000000", "#1C4A86"))
            .buildSDK()

    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {

            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    val payModels = PayModels("QRIS", result.response.transactionId)
                    val db = FirebaseFirestore.getInstance()
                    val tambahPayment = db.collection("ListPesanan")
                        .document(intent.extras?.getString("DocID").toString())
                        .collection("Payment").document("detailPayment")
                    tambahPayment.set(payModels)
                        .addOnSuccessListener {
                            Log.d("Payment", "onTransactionFinished: Berhasil Menyimpan")
                        }.addOnFailureListener {
                            Log.e("Payment", "onTransactionFinished: Gagal Menyimpan", it.cause)
                        }

                    Toast.makeText(
                        this,
                        "Transaction Sukses " + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                    ubahOrder("Success")


                }

                TransactionResult.STATUS_PENDING -> {
                    Toast.makeText(
                        this,
                        "Transaction Pending " + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                    ubahOrder("Pending")
                }

                TransactionResult.STATUS_FAILED -> {
                    Toast.makeText(
                        this,
                        "Transaction Failed" + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                    ubahOrder("Failed")
                }
            }
            result.response.statusMessage
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show()
            ubahOrder("Pending")
        } else {
            if (result.status.equals((TransactionResult.STATUS_INVALID), ignoreCase = true)) {
                Toast.makeText(
                    this,
                    "Transaction Invalid" + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
                ubahOrder("Pending")
                Log.d("Pembayaran", "onTransactionFinished: ${result.response.transactionId}")
            } else {
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clickPay() {
        try {
            MidtransSDK.getInstance().transactionRequest = transactionRequest(intent!!.getStringExtra("DocID"),binding.textView36.text.toString().toDouble().toInt(), 1, intent!!.getStringExtra("NamaService"));
            MidtransSDK.getInstance().startPaymentUiFlow(this);
        }catch (e:Exception){

            Log.d("Error", "clickPay: ${binding.textView36.text.toString().toDouble()}")
            Log.d("Error", "clickPay: ${System.currentTimeMillis()}")
            Log.d("Error", "clickPay: ${auth.currentUser!!.email}")
            e.printStackTrace()
        }

//
    }

    fun transactionRequest(id: String?, price: Int, qty: Int, name: String?): TransactionRequest {
        auth = FirebaseAuth.getInstance()
        val request = TransactionRequest(System.currentTimeMillis().toString(), binding.textView36.text.toString().toDouble())
        //        request.setCustomerDetails(customerDetails());
        val details = ItemDetails(id, price.toDouble(), qty, name)
        val itemDetails = ArrayList<ItemDetails>()
        itemDetails.add(details)
        request.itemDetails = itemDetails
        request.customerDetails = CustomerDetails("Pembeli","Laundry",auth.currentUser!!.email,"08962342384")
        val creditCard = CreditCard()
        creditCard.isSaveCard = false
        creditCard.authentication = CreditCard.MIGS
        request.creditCard = creditCard
        return request
    }

    fun ubahOrder(status: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("ListPesanan").document(intent.extras?.getString("DocID").toString())
            .update("paymentStatus", status)
            .addOnSuccessListener {
                Log.d("Update Payment", "ubahOrder: berhasil Mengubah")
            }
            .addOnFailureListener {
                Log.e("Update Payment", "ubahOrder: gagal mengubah", it)
            }

    }

    override fun onStart() {
        super.onStart()
        openReceiptActivity()
    }

    fun formatter(date: Date?): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return formatter.format(date!!)
    }

    private fun openReceiptActivity(){
        val db = FirebaseFirestore.getInstance()
        db.collection("ListPesanan").document(intent.extras?.getString("DocID").toString()).get()
            .addOnSuccessListener {
                if (it.getString("paymentStatus").toString() == "Success") {
                    val intent = Intent(this, ReceiptActivity::class.java)
                    intent.putExtra("orderID", this.intent.extras!!.getString("DocID"))
                    intent.putExtra("orderName", this.intent.extras!!.getString("NamaService"))
                    intent.putExtra(
                        "serviceSatuan",
                        this.intent.extras!!.getString("ServiceSatuan")
                    )
                    intent.putExtra("banyak", this.intent.extras!!.getString("BanyakService"))
                    intent.putExtra("total", this.intent.extras!!.getString("TotalService"))
                    intent.putExtra(
                        "hasilPerkalian",
                        this.intent.extras!!.getString("jumlahHargaService")
                    )
                    intent.putExtra("tanggalOrder", formatter(it.getDate("orderDate")))
                    startActivity(intent)
                    finish()
                }
            }

    }



}