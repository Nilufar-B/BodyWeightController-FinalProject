package com.example.final_projectxml.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.RatingBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.final_projectxml.R
import com.example.final_projectxml.api.AdviceViewModel
import com.example.final_projectxml.databinding.FragmentAboutBinding
import kotlinx.coroutines.launch


class About : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private var selectedRating: Float = 0f

    private val adviceViewModel by viewModels<AdviceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(layoutInflater,container,false)
        val view = binding.root

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            selectedRating = rating
            when (rating.toInt()) {
                1 -> binding.ratingImage.setImageResource(R.drawable.onestar)
                2 -> binding.ratingImage.setImageResource(R.drawable.twostar)
                3 -> binding.ratingImage.setImageResource(R.drawable.threestar)
                4 -> binding.ratingImage.setImageResource(R.drawable.fourstar)
                5 -> binding.ratingImage.setImageResource(R.drawable.fivestar1)
            }
            animateImage(binding.ratingImage)
        }

        binding.btnGetAdvice.setOnClickListener {
            lifecycleScope.launch {
                val randomAdvice = adviceViewModel.loadRandomAdvice()
                if (randomAdvice != null){
                    binding.tvAdvice.text = randomAdvice.toString()
                }
            }
        }

        return view
    }

    private fun animateImage(ratingImage: ImageView){
        val scaleAnimation = ScaleAnimation(
            0f,
            1f,
            1f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnimation.duration = 200
        scaleAnimation.fillAfter = true
        ratingImage.startAnimation(scaleAnimation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}