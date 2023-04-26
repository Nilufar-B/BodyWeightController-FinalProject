package com.example.final_projectxml.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.final_projectxml.R
import com.example.final_projectxml.databinding.FragmentNavycalcBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log
import kotlin.math.log10

class NavyCalc : Fragment() {

    private var _binding: FragmentNavycalcBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNavycalcBinding.inflate(inflater, container, false)
       val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculate.setOnClickListener{
           val height = binding.etHeight.text.toString().toDouble()
            val neck = binding.etNeck.text.toString().toDouble()
            val waist = binding.etWaist.text.toString().toDouble()
            val hip = binding.etHips.text.toString().toDouble()

            val bodyFatPercentage = calculatePercentage(height, neck, waist, hip)

            binding.NavyResult.visibility = View.VISIBLE
            binding.tvNavyValue.text = "$bodyFatPercentage%"
            binding.tvNavyDescription.text = "Your body fat is normal."
        }
    }

    private fun calculatePercentage(height: Double, neck: Double, waist: Double, hip: Double): BigDecimal? {

        val heightValue = height
        val neckValue = neck
        val waistValue = waist
        val hipValue = hip

        val bodyFat = 495 / (1.0324 - 0.19077 *
                (log10(waistValue+hipValue-neckValue))+0.22100*(log10(heightValue))) - 450


        val result = BigDecimal(bodyFat).setScale(2, RoundingMode.HALF_EVEN)

        return  result

    }


}
