package com.olivia.laundry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.olivia.laundry.databinding.ActivityKalkulatorBinding

class KalkulatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKalkulatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKalkulatorBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_kalkulator)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}