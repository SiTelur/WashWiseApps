package com.olivia.laundry.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.adapter.PesananAdapter
import com.olivia.laundry.databinding.FragmentPesananBinding
import com.olivia.laundry.models.JenisPesananModels
import com.olivia.laundry.models.PesananModels
import com.olivia.laundry.models.ProgressModels

/**
 * A simple [Fragment] subclass.
 * Use the [PesananFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PesananFragment : Fragment() {
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

    lateinit var binding: FragmentPesananBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPesananBinding.inflate(inflater)

        val query: Query = FirebaseFirestore.getInstance().collection("JenisPesanan")


        val option = FirestoreRecyclerOptions.Builder<JenisPesananModels>()
            .setQuery(query, JenisPesananModels::class.java)
            .build()

        val adapter = PesananAdapter(option)
        binding.rvPesanan.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
        binding.rvPesanan.adapter = adapter
        adapter.listener = object :PesananAdapter.CheckboxListener{
            override fun onCheckboxClicked(model: Int?, documentID: String?) {
                Log.d("PesananFragment", "Harga : $model, DocumentID : $documentID")
                binding.textView49.text = model.toString()
                binding.button4.isEnabled = model != 0
                binding.textView53.text = documentID.toString()
            }

        }
        adapter.startListening()
        if (binding.checkBox6.isChecked){
            Log.d("PesananFragment", "onCreateView: gunakanVoucher")
        }else{
            Log.d("PesananFragment", "onCreateView: tidakmenggunakanVoucher")
        }

        val user = Firebase.auth.currentUser
        binding.button4.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val pesananModels = PesananModels("Order Create",user?.uid,
                Timestamp.now(),binding.textView53.text.toString(),0)
            val progressModels = ProgressModels(Timestamp.now(),"PesananDibuat")

            if (binding.checkBox6.isChecked){
                Log.d("PesananFragment", "onCreateView: gunakanVoucher")
            }else{
                Log.d("PesananFragment", "onCreateView: tidakmenggunakanVoucher")
            }
            db.collection("ListPesanan").add(pesananModels).addOnSuccessListener {
                db.collection("ListPesanan").document(it.id).collection("progress").add(progressModels).addOnSuccessListener {
                    Log.d("PesananFragment", "Berhasil Menyimpan")
                }
            }
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
         * @return A new instance of fragment PesananFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): PesananFragment {
            val fragment = PesananFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}