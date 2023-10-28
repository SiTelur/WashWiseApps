package com.olivia.laundry.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.LoginActivity
import com.olivia.laundry.MainActivity
import com.olivia.laundry.R
import com.olivia.laundry.databinding.FragmentUserBinding
import com.olivia.laundry.dialoguser.ChangeEmailFragment
import com.olivia.laundry.dialoguser.ChangePasswordFragment
import com.olivia.laundry.dialoguser.ChangeUserDetailFragment
import com.olivia.laundry.dialoguser.TambahAlamatFragment


/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }
    private lateinit var binding: FragmentUserBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater)
        auth = Firebase.auth
        val user = auth.currentUser
        val db = Firebase.firestore

        binding.textView44.text = auth.currentUser?.displayName
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.userLogOut -> {
                    auth.signOut()
                    startActivity(Intent(activity, LoginActivity::class.java))
                    return@setOnMenuItemClickListener true
                }

                else -> {return@setOnMenuItemClickListener false}
            }
        }
        var alamat = ""
        user?.let {
            binding.textView2.text = it.displayName
            db.collection("User").document(it.uid).get().addOnSuccessListener { data ->
                alamat = data.getString("address").toString()
            }
        }
        binding.cvAlamat.setOnClickListener {
            val tambahAlamatFragment = TambahAlamatFragment.newInstance(alamat,null)
            tambahAlamatFragment.show(childFragmentManager,"ShowAlamat")
        }

        binding.cvEmail.setOnClickListener {
            ChangeEmailFragment().show(childFragmentManager,"ShowChangeEmail")
        }

        binding.cvPassword.setOnClickListener {
            ChangePasswordFragment().show(childFragmentManager,"ShowChangePassword")
        }

        binding.imageButton.setOnClickListener {
            ChangeUserDetailFragment().show(childFragmentManager,"ShowDetailUser")
        }


        return binding.root
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}