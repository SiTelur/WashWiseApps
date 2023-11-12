package com.olivia.laundry.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.adapter.RiwayatAdapter
import com.olivia.laundry.databinding.FragmentRiwayatBinding
import com.olivia.laundry.fragment.riwayat.BerlangsungFragment
import com.olivia.laundry.fragment.riwayat.SelesaiFragment
import com.olivia.laundry.models.PesananModels


/**
 * A simple [Fragment] subclass.
 * Use the [RiwayatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RiwayatFragment : Fragment() {
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
    lateinit var binding: FragmentRiwayatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRiwayatBinding.inflate(inflater)
        val user = Firebase.auth.currentUser

        binding.tabLayout.getTabAt(0)?.select()



        val nextFrag = BerlangsungFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(binding.Fl.id, nextFrag, "BerlangsungFragment")
            ?.addToBackStack(null)
            ?.commit();

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d("Tab", "onTabSelected: $tab")
                when (tab.position) {
                    0 -> {


                        val nextFrag = BerlangsungFragment()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(binding.Fl.id, nextFrag, "BerlangsungFragment")
                            ?.addToBackStack(null)
                            ?.commit();
//                        val query = FirebaseFirestore.getInstance().collection("ListPesanan").whereEqualTo("uid",user?.uid)
//                        val option = FirestoreRecyclerOptions.Builder<PesananModels>()
//                            .setQuery(query, PesananModels::class.java)
//                            .build()
//
//                        val adapter = RiwayatAdapter(option)
//                        binding.rvRiwayat.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
//                        binding.rvRiwayat.adapter = adapter
//                        adapter.startListening()
                    }

                    1 -> {
                        val nextFrag = SelesaiFragment()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(binding.Fl.id, nextFrag, "BerlangsungFragment")
                            ?.addToBackStack(null)
                            ?.commit();
//                        val query = FirebaseFirestore.getInstance().collection("ListPesanan").whereEqualTo("uid",user?.uid).whereNotEqualTo("orderStatus","Selesai")
//                        val option = FirestoreRecyclerOptions.Builder<PesananModels>()
//                            .setQuery(query, PesananModels::class.java)
//                            .build()
//
//                        val adapter = RiwayatAdapter(option)
//                        binding.rvRiwayat.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
//                        binding.rvRiwayat.adapter = adapter
//                        adapter.startListening()
//
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("Tab", "onTabUnselected: $tab")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("Tab", "onTabReselected: $tab")
            }
        })
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//               Log.d("TAG", "onTabUnselected: $tab")
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                Log.d("TAG", "onTabReselected: $tab")
//            }
//
//            //...
//        })

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
         * @return A new instance of fragment RiwayatFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): RiwayatFragment {
            val fragment = RiwayatFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}
