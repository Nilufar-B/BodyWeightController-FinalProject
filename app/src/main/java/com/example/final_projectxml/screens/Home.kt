package com.example.final_projectxml.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.final_projectxml.R
import com.example.final_projectxml.database.UserDataApplication
import com.example.final_projectxml.database.UserDataDao
import com.example.final_projectxml.database.UserDataEntity
import com.example.final_projectxml.databinding.FragmentHomeBinding
import com.example.final_projectxml.databinding.FragmentJournalBinding
import kotlinx.coroutines.launch


class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        val userDataDao = (requireActivity().application as UserDataApplication).db.userDataDao()

        binding?.btnAddData?.setOnClickListener{
            addData(userDataDao)
        }


        return view

    }

    fun addData (userDataDao: UserDataDao){
        val weight = binding?.etWeightDaily?.text.toString()
        val calories = binding?.etCalories?.text.toString()
        val steps = binding?.etSteps?.text.toString()

        //check if fields is not empty
        if (weight.isNotEmpty() && calories.isNotEmpty() && steps.isNotEmpty()){
            lifecycleScope.launch{
                userDataDao.insert(UserDataEntity(weight=weight.toFloat(), calories = calories.toInt(), steps = steps.toInt()  ))
                Toast.makeText(requireContext(), "Data saved!", Toast.LENGTH_LONG).show()
               //clear fields when we press the button
                binding?.etWeightDaily?.text?.clear()
                binding?.etCalories?.text?.clear()
                binding?.etSteps?.text?.clear()
            }
        }else{
            //show toast if fields is empty
            Toast.makeText(requireContext(),
            "Fields cannot be blank.",
            Toast.LENGTH_LONG).show()
        }
    }

}