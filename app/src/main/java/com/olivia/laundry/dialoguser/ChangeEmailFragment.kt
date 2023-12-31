package com.olivia.laundry.dialoguser

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.R
import com.olivia.laundry.databinding.FragmentChangeEmailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeEmailFragment : DialogFragment() {
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
    lateinit var binding: FragmentChangeEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeEmailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        binding.button5.setOnClickListener{
            val user = Firebase.auth.currentUser

            if (binding.editTextTextEmailAddress.length() < 6){
                return@setOnClickListener
            }

            if (!isValidEmail(binding.editTextTextEmailAddress.text)){
                return@setOnClickListener
            }


            user!!.updateEmail(binding.editTextTextEmailAddress.text.toString())
                .addOnSuccessListener {
                    Log.d("ChangeEmail", "Email has been change.")
                    Toast.makeText(requireContext(), "Berhasil Mengubah Email", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener {
                    Log.e("ChangeEmail", "onCreateView: Gagal",it)
                }

        }

        binding.toolbar2.setNavigationOnClickListener {
            dismiss()
        }


        return binding.root
    }
    private fun isValidEmail(target: CharSequence?): Boolean {
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches()
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
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): ChangeEmailFragment {
            val fragment = ChangeEmailFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}