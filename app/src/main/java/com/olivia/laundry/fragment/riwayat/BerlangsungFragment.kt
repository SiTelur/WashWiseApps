package com.olivia.laundry.fragment.riwayat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.R
import com.olivia.laundry.adapter.HomeAdapter
import com.olivia.laundry.adapter.RiwayatAdapter
import com.olivia.laundry.databinding.FragmentBerlangsungBinding
import com.olivia.laundry.models.PesananModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BerlangsungFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BerlangsungFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentBerlangsungBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBerlangsungBinding.inflate(inflater)
        val user = Firebase.auth.currentUser
        val query = FirebaseFirestore.getInstance().collection("ListPesanan").whereEqualTo("uid",user?.uid).whereNotEqualTo("orderStatus","Pesanan Selesai")

        val option = FirestoreRecyclerOptions.Builder<PesananModels>()
            .setQuery(query, PesananModels::class.java)
            .build()

        val adapter = RiwayatAdapter (option)
        binding.rvBerlangsung.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
        binding.rvBerlangsung.adapter = adapter
        adapter.startListening()
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BerlangsungFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BerlangsungFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}