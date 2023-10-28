package com.olivia.laundry


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.olivia.laundry.databinding.ActivityMainBinding
import com.olivia.laundry.fragment.HomeFragment
import com.olivia.laundry.fragment.PesananFragment
import com.olivia.laundry.fragment.RiwayatFragment
import com.olivia.laundry.fragment.UserFragment
import com.olivia.laundry.viewpager.MainViewPager


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MainViewPager

    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener{ item ->
        when (item.itemId) {
            R.id.home -> {
                binding.fL.currentItem = 0
                return@OnItemSelectedListener true
            }
            R.id.pesanan -> {
                binding.fL.currentItem = 1
                return@OnItemSelectedListener true
            }
            R.id.riwayat -> {
                binding.fL.currentItem = 2
                return@OnItemSelectedListener true
            }
            R.id.user -> {
                binding.fL.currentItem = 3
                return@OnItemSelectedListener true
            }
            else -> {binding.fL.currentItem = 0
                return@OnItemSelectedListener true}
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        myAdapter = MainViewPager(supportFragmentManager,lifecycle)

        // add Fragments in your ViewPagerFragmentAdapter class

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(HomeFragment())
        myAdapter.addFragment(PesananFragment())
        myAdapter.addFragment(RiwayatFragment())
        myAdapter.addFragment(UserFragment())


        // set Orientation in your ViewPager2

        // set Orientation in your ViewPager2
        binding.fL.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.fL.adapter = myAdapter

        binding.bottomNavBar.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.fL.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavBar.menu.findItem(R.id.home).isChecked = true
                    1 -> binding.bottomNavBar.menu.findItem(R.id.pesanan).isChecked = true
                    2 -> binding.bottomNavBar.menu.findItem(R.id.riwayat).isChecked = true
                    3 -> binding.bottomNavBar.menu.findItem(R.id.user).isChecked = true
                    else -> 0
                }

            }
        })

        /* Tambahkan jika nanti membuat fragment terbaru */
//        binding.bottomNavBar.setOnItemSelectedListener{
//            binding.fL.currentItem = when(it.itemId){
//                R.id.home -> binding.bottomNavBar.bottom
//                R.id.pesanan -> 1
//                else -> 0
//            }
//
//
//
//            true
//        }

//        binding.bottomNavBar.setOnItemSelectedListener {
//            when (it.getItemId()) {
//                R.id.home -> binding.fL.currentItem = 0
//                R.id.pesanan -> binding.fL.currentItem = 1
//
//            }
//            true
//        }


    }
}