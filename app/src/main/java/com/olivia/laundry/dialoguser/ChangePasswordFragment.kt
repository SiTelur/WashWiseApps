package com.olivia.laundry.dialoguser

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.R
import com.olivia.laundry.databinding.FragmentChangeEmailBinding
import com.olivia.laundry.databinding.FragmentChangePasswordBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }
    lateinit var binding: FragmentChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater)

        binding.checkBox4.setOnCheckedChangeListener { _, isChecked -> // If the checkbox is checked, show the password.
            // Otherwise, hide the password.
            binding.editTextTextPassword2.transformationMethod = if (isChecked) null else PasswordTransformationMethod.getInstance()
            binding.editTextTextPassword2.clearFocus()
        }

        binding.button5.setOnClickListener {
            if (binding.editTextTextPassword2.length() < 6){
                return@setOnClickListener
            }

            val user = Firebase.auth.currentUser
            val newPassword = binding.editTextTextPassword2.text.toString()

            user!!.updatePassword(newPassword)
                .addOnSuccessListener {
                    Log.d("ChangePass", "Berhasil Mengganti Password")
                }
                .addOnFailureListener {
                    Log.e("ChangePass", "onCreateView: Gagal",it)
                }

            dismiss()
        }

        binding.toolbar2.setNavigationOnClickListener {
            dismiss()
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
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): ChangePasswordFragment {
            val fragment = ChangePasswordFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}