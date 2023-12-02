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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
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

        binding.checkBox3.setOnCheckedChangeListener { _, isChecked -> // If the checkbox is checked, show the password.
            // Otherwise, hide the password.
            binding.txtPassword.transformationMethod = if (isChecked) null else PasswordTransformationMethod.getInstance()
            binding.txtPassword.clearFocus()
        }

        binding.btnRegister.setOnClickListener {

            if (binding.txtEmail.text.isEmpty()){
                Toast.makeText(this, "Masukkan Email Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtPassword.text.isEmpty()){
                Toast.makeText(this, "Masukkan Password Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtNama.text.isEmpty()){
                Toast.makeText(this, "Masukkan Nama Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtNomerTelepon.text.isEmpty()){
                Toast.makeText(this, "Masukkan Nomor Telepon Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar2.visibility = View.VISIBLE;
            binding.txtNama.clearFocus()
            binding.txtNomerTelepon.clearFocus()
            binding.txtEmail.clearFocus()
            binding.txtPassword.clearFocus()
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            auth.createUserWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString())
                .addOnSuccessListener {


                    val  profileUpdate = userProfileChangeRequest {
                        displayName = binding.txtNama.text.toString()
                    }



                    val usersModels = UsersModels(binding.txtNama.text.toString(),null,binding.txtNomerTelepon.text.toString())
                    auth.currentUser?.let { it1 -> db.collection("User").document(it1.uid).set(usersModels) }
                        ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        ?.addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    Toast.makeText(this, "Berhasil Registrasi", Toast.LENGTH_SHORT).show()
                    binding.progressBar2.visibility = View.GONE;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    val user = Firebase.auth.currentUser
                    user!!.updateProfile(profileUpdate).addOnSuccessListener {
                        Log.d(TAG, "User profile updated.")
                    }

                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    binding.progressBar2.visibility = View.GONE;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.w(this.toString(), "onCreate: Gagal", it)
                }
        }
    }
}