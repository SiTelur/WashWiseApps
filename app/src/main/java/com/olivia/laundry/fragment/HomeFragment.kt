package com.olivia.laundry.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.KalkulatorActivity
import com.olivia.laundry.MainActivity
import com.olivia.laundry.R
import com.olivia.laundry.adapter.HomeAdapter
import com.olivia.laundry.databinding.FragmentHomeBinding
import com.olivia.laundry.models.PesananModels
import com.olivia.laundry.models.UsersModels

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {
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
private lateinit var binding: FragmentHomeBinding
     var registration: ListenerRegistration? = null
    lateinit var auth:FirebaseAuth
    var voucher:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.userName.text
        val db = FirebaseFirestore.getInstance()
auth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser
        binding.userName.text = user?.displayName
        Log.d("HomeFragment", "onCreateView: ${user?.displayName}")
        Log.d("HomeFragment", "onCreateView: ${user?.uid}")

        binding.kalkulator.setOnClickListener {
            startActivity(Intent(activity,KalkulatorActivity::class.java))
        }
        val query = FirebaseFirestore.getInstance().collection("ListPesanan").whereEqualTo("uid",user?.uid).whereNotEqualTo("orderStatus","Pesanan Telah Selesai")




        registration = db.collection("User").document(user?.uid.toString()).addSnapshotListener{ snapshot, _ ->
            if(snapshot == null) {
                // Handle case of no data yet
                return@addSnapshotListener
            }

            binding.progressBar4.setProgress(((snapshot.get("voucher").toString().toDouble() / 12 * 100).toInt()),true)
            if (snapshot.get("voucher").toString().toDouble() >= 12.0){
                binding.textView71.text = "Voucher Anda Siap Digunakan"
            }else{
                binding.textView71.text = "Voucher Anda Sekarang ${snapshot!!.get("voucher").toString()} Kurang ${(12 - snapshot!!.get("voucher").toString().toDouble()).toInt()} Lagi"
            }
        }


//        binding.progressBar4.setProgress(20,true)

        val option = FirestoreRecyclerOptions.Builder<PesananModels>()
            .setQuery(query,PesananModels::class.java)
            .build()

        val adapter = HomeAdapter(option)
        binding.homeRV.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
        binding.homeRV.adapter = adapter
        adapter.startListening()
        // Inflate the layout for this fragment
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}