package com.olivia.laundry.dialoguser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.R
import com.olivia.laundry.databinding.FragmentChangeUserDetailBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeUserDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeUserDetailFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }
    lateinit var binding: FragmentChangeUserDetailBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangeUserDetailBinding.inflate(inflater)
        val db = Firebase.firestore
        auth = Firebase.auth

        val namaAwal = auth.currentUser?.displayName

        val datanomorTelepon = db.collection("User").document(auth.currentUser!!.uid).get()
        datanomorTelepon.addOnCompleteListener {
            if (it.isSuccessful){
                binding.txtNamaEditProfil.setText(namaAwal.toString())
                binding.txtNamaEditProfil2.setText(it.result.getString("phoneNumber").toString())
            }
        }

        val homerHp = binding.txtNamaEditProfil.text.toString()

        binding.toolbar2.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.saveLocation -> {
                    val user = Firebase.auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = binding.txtNamaEditProfil.text.toString()
                    }
                    user!!.updateProfile(profileUpdates).addOnSuccessListener {
                        Log.d("ChangeUserDetailFragment", "UserProfile Updated")
                    }
                    db.collection("User").document(user.uid).update("phoneNumber",binding.txtNamaEditProfil2.text.toString(),"name",binding.txtNamaEditProfil.text.toString())
                    Toast.makeText(activity, "Nama Anda Berhasil Diganti, Mohon Restart Aplikasi", Toast.LENGTH_SHORT).show()
                    dismiss()
                   true

                }

                else -> {false}
            }
        }

        binding.toolbar2.setNavigationOnClickListener {
            dismiss()
        }



        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangeUserDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangeUserDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}