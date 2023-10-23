package com.olivia.laundry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.databinding.ActivityRegisterBinding
import com.olivia.laundry.models.UsersModels


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    val TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.btnRegister.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString())
                .addOnSuccessListener {
                    val usersModels = UsersModels(auth.uid,null,binding.txtNomerTelepon.text.toString())
                    auth.currentUser?.let { it1 -> db.collection("User").document(it1.uid).set(usersModels) }
                        ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        ?.addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    Toast.makeText(this, "Berhasil Registrasi", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }
                .addOnFailureListener {
                    Log.w(this.toString(), "onCreate: Gagal", it)
                }
        }
    }
}