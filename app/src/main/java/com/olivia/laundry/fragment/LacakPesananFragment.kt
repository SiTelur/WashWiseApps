package com.olivia.laundry.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.olivia.laundry.adapter.LacakPesananAdapter
import com.olivia.laundry.adapter.RiwayatAdapter
import com.olivia.laundry.databinding.FragmentLacakPesananBinding
import com.olivia.laundry.models.ProgressModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LacakPesananFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LacakPesananFragment : DialogFragment() {
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
    lateinit var binding: FragmentLacakPesananBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLacakPesananBinding.inflate(inflater)

        val query = FirebaseFirestore.getInstance().collection("ListPesanan").document(param1.toString()).collection("progress").orderBy("progressDate",
            Query.Direction.DESCENDING)
        val option =  FirestoreRecyclerOptions.Builder<ProgressModels>()
            .setQuery(query, ProgressModels::class.java)
            .build()

        val adapter = LacakPesananAdapter(option)
        binding.rvLacakPesanan.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
        binding.rvLacakPesanan.adapter = adapter
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
         * @return A new instance of fragment LacakPesananFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String?) =
            LacakPesananFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}