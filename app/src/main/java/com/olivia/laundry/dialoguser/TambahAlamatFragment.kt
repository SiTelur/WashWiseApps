package com.olivia.laundry.dialoguser

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.olivia.laundry.R
import com.olivia.laundry.databinding.FragmentTambahAlamatBinding
import com.olivia.laundry.models.AlamatModels
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


/**
 * A simple [Fragment] subclass.
 * Use the [TambahAlamatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TambahAlamatFragment : DialogFragment() {
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

    lateinit var binding: FragmentTambahAlamatBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTambahAlamatBinding.inflate(inflater)
        binding.editTextText.setText(mParam1.toString())
        verifyStoragePermissions(activity)
        val db = Firebase.firestore
        initializeMaps()
        auth = Firebase.auth
        val mapController = binding.mapView.controller
        val startPoint = GeoPoint(-0.789275, 113.921327)
        mapController.setZoom(5.0)
        mapController.setCenter(startPoint)

        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), binding.mapView)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()

        if (!mLocationOverlay.isMyLocationEnabled) {
            mapController.setZoom(5.0)
            mLocationOverlay.runOnFirstFix {
                mLocationOverlay.lastFix
                binding.mapView.controller.setCenter(mLocationOverlay.myLocation)
                Log.d("Location", "onCreateView: ${mLocationOverlay.myLocation}")

            }

        } else {
            mapController.setZoom(15.0)
        }
        var geoPoint: com.google.firebase.firestore.GeoPoint? = null


        Log.d("LocationDiterima", "onCreateView: $geoPoint")


        binding.mapView.overlays.add(mLocationOverlay)

        binding.mapView.invalidate()



        binding.toolbar.setOnMenuItemClickListener {
            if (binding.editTextText.text.equals("null")) {
                Toast.makeText(context, "Anda Belum Mengisi Detail Alamat", Toast.LENGTH_SHORT).show()
                return@setOnMenuItemClickListener true
            }

            when (it.itemId) {
                R.id.saveLocation -> {
                    geoPoint = com.google.firebase.firestore.GeoPoint(
                        mLocationOverlay.myLocation.latitude,
                        mLocationOverlay.myLocation.longitude
                    )
                    val alamatModels = AlamatModels(binding.editTextText.text.toString(), geoPoint)
                    val update = hashMapOf<String, Any?>(
                        "address" to alamatModels.address,
                        "coordinatePoint" to alamatModels.coordinatePoint
                    )
                        auth.currentUser?.uid?.let { it1 ->
                            db.collection("User").document(it1).update(update).addOnSuccessListener {
                            }.addOnFailureListener {
                                Log.d("UpdateData", "Berhasil")
                            }.addOnFailureListener {
                                Log.d("UpdateData", "Gagal")
                            }
                        }
                        Log.d("LocationDikirim", "onCreateView: $geoPoint")
                        dismiss()
                        return@setOnMenuItemClickListener true

                }

                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {

            dismiss()
        }
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
         * @return A new instance of fragment TambahAlamat.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): TambahAlamatFragment {
            val fragment = TambahAlamatFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private fun initializeMaps() {
        Configuration.getInstance().userAgentValue = BuildConfig.BUILD_TYPE
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setScrollableAreaLimitDouble(BoundingBox(85.0, 180.0, -85.0, -180.0))
        binding.mapView.maxZoomLevel = 20.0
        binding.mapView.minZoomLevel = 4.0
        binding.mapView.isHorizontalMapRepetitionEnabled = false
        binding.mapView.isVerticalMapRepetitionEnabled = false
        binding.mapView.setScrollableAreaLimitLatitude(
            MapView.getTileSystem().maxLatitude,
            MapView.getTileSystem().minLatitude, 0
        )
    }

    private val REQUEST_GPS = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    private fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE, REQUEST_GPS
            )
        }
    }
}