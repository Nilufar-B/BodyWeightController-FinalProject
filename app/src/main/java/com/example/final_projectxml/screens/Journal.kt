package com.example.final_projectxml.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.final_projectxml.databinding.FragmentJournalBinding
import java.math.BigDecimal
import java.math.RoundingMode

class Journal : Fragment() {

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJournalBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding?.btnCalculateBMI?.setOnClickListener{
            if (validateMetricUnits()){

                val heightValue: Float = binding?.etHeight?.text.toString().toFloat() / 100 //to get height value in meters
                val weightValue: Float = binding?.etWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue*heightValue)

                displayBMIResults(bmi)
            } else{
                Toast.makeText(requireContext(), "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()



            }
        }

        return view
    }

    private fun displayBMIResults(bmi:Float){

        val bmiLable:String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0){
            bmiLable = "Very severaly Underweight"
            bmiDescription = "Underweight, you need to eat more!Take care of yourself!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLable = "Severely underweight"
            bmiDescription = "Underweight, you need to eat more!Take care of yourself!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLable = "Underweight"
            bmiDescription = "Underweight, you need to eat more!Take care of yourself!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLable = "Normal"
            bmiDescription = "You are in a good shape!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLable = "Overweight"
            bmiDescription = "You need to take care of yourself!Track calories and activity"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLable = "Obesity"
            bmiDescription = "Act now! Take care of yourself!"
        }else{
            bmiLable = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! Pull yourself together before it's too late!"
        }


        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString() //cat value to display

        binding?.bmiResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLable
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun validateMetricUnits(): Boolean{
       var isValid = true

        if(binding?.etWeight?.text.toString().isEmpty()){
            isValid = false
        } else if(binding?.etHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}