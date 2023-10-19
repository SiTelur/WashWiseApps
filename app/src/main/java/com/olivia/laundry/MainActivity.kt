package com.olivia.laundry

import android.os.Bundle
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.olivia.laundry.databinding.ActivityMainBinding
import com.olivia.laundry.fragment.HomeFragment
import com.olivia.laundry.viewpager.MainViewPager


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: MainViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        myAdapter = MainViewPager(supportFragmentManager,lifecycle)

        // add Fragments in your ViewPagerFragmentAdapter class

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(HomeFragment())


        // set Orientation in your ViewPager2

        // set Orientation in your ViewPager2
        binding.fL.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.fL.adapter = myAdapter

        /* Tambahkan jika nanti membuat fragment terbaru */
        binding.bottomNavBar.setOnItemSelectedListener{
            binding.fL.currentItem = when(it.itemId){
                R.id.home -> 0
                else -> 0
            }

            true
        }
    }
}