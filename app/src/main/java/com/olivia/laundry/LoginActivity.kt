package com.olivia.laundry

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.olivia.laundry.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        binding.checkBox2.setOnCheckedChangeListener { _, isChecked -> // If the checkbox is checked, show the password.
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
                    val checkUser = db.collection("UserAdmin").document(auth.currentUser?.uid.toString())
                    checkUser.get()
                        .addOnSuccessListener {
                            if (it.getBoolean("admin") == true){
                                Log.d("Login", "onCreate: Data adalah ${it.getBoolean("admin")}")
                                Toast.makeText(this, "Anda Harus Login Dengan Email Customer", Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE;
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                auth.signOut()
                            }else{
                                Log.d(this.toString(), "signInWithEmail:success")
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                                binding.progressBar.visibility = View.GONE;
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                            Log.d("Login", "onCreate: Ada Data")
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Anda Harus Login Dengan Email Customer", Toast.LENGTH_SHORT).show()
                            auth.signOut()
                        }
                }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.w(this.toString(), "createUserWithEmail:failure", it)
                    Toast.makeText(this, "Email Atau Password Anda Salah", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }

        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.textView43.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordActivity::class.java))
        }

    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    fun checkUser(uid:String):Boolean{
        var kondisi:Boolean = true
        val db = FirebaseFirestore.getInstance()
        val checkUser = db.collection("User").document(uid)
        checkUser.get()
            .addOnSuccessListener {
                kondisi = true
            }
            .addOnFailureListener {
                kondisi =   false
            }
        return kondisi
    }
}