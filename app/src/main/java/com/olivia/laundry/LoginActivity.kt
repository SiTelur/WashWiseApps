package com.olivia.laundry

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.olivia.laundry.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.checkBox2.setOnCheckedChangeListener { buttonView, isChecked -> // If the checkbox is checked, show the password.
            // Otherwise, hide the password.
            binding.txtPasswordLogin.transformationMethod = if (isChecked) null else PasswordTransformationMethod.getInstance()
            binding.txtPasswordLogin.clearFocus()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.txtEmailLogin.text.isEmpty()){
                Toast.makeText(this, "Masukkan Email Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtPasswordLogin.text.isEmpty()){
                Toast.makeText(this, "Masukkan Password Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE;
            binding.txtEmailLogin.clearFocus()
            binding.txtEmailLogin.clearFocus()
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            auth.signInWithEmailAndPassword(binding.txtEmailLogin.text.toString(),binding.txtPasswordLogin.text.toString())
                .addOnSuccessListener {
                    Log.d(this.toString(), "signInWithEmail:success")
                    startActivity(Intent(this,MainActivity::class.java))
                    binding.progressBar.visibility = View.GONE;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.w(this.toString(), "createUserWithEmail:failure", it)
                    Toast.makeText(this, "Email Atau Password Anda Salah", Toast.LENGTH_SHORT).show()
                }

        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}