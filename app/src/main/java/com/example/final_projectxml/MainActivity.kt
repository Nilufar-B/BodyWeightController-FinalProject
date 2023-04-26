package com.example.final_projectxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.final_projectxml.databinding.ActivityMainBinding
import com.example.final_projectxml.screens.About
import com.example.final_projectxml.screens.Home
import com.example.final_projectxml.screens.BMI
import com.example.final_projectxml.screens.NavyCalc

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val homeFragment = Home()
        val bmiFragment = BMI()
        val navyCalcFragment = NavyCalc()
        val aboutFragment = About()

        replaceFragment(homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.bmi -> replaceFragment(bmiFragment)
                R.id.navycalc -> replaceFragment(navyCalcFragment)
                R.id.about -> replaceFragment(aboutFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }

    }
}