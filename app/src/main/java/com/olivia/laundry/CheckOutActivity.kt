package com.olivia.laundry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder.Companion.CLIENT_KEY
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.uikit.external.UiKitApi
import com.olivia.laundry.databinding.ActivityCheckOutBinding

class CheckOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        var selectedPayment = ""
        setContentView(binding.root)

        binding.textView15.text = intent.extras?.getString("DocID")
        binding.textView61.text = intent.extras?.getString("BanyakService")
        binding.textView60.text = intent.extras?.getString("ServiceSatuan")
        binding.textView36.text = intent.extras?.getString("TotalService")
        binding.textView59.text = intent.extras?.getString("TotalService")
        binding.textView23.text = intent.extras?.getString("TotalService")

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                binding.radioButton.id -> {
                    selectedPayment = "qris"
                }
                binding.radioButton2.id -> {
                    selectedPayment = "cod"
                }
            }
        }

        binding.button2.setOnClickListener {
            when(selectedPayment) {
                "qris" -> {

                    Log.d("CheckOutActivity", "onCreate: Anda Menggunakan QRIS")
                }
                "cod" -> {
                    Log.d("CheckOutActivity", "onCreate: Anda Menggunakan COD")
                }
                else -> Toast.makeText(this, "Anda Harus Memilih 1 Pembayaran", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}