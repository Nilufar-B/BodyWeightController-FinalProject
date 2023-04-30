package com.example.final_projectxml.screens

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_projectxml.R
import com.example.final_projectxml.database.*
import com.example.final_projectxml.databinding.FragmentHomeBinding
import com.example.final_projectxml.databinding.UpdateDataBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


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
        lifecycleScope.launch {
            userDataDao.fetchAllData().collect{
                val list = ArrayList(it)
                setupListOfData(list, userDataDao)
            }
        }
        return view
    }



    //create function to add data
    fun addData (userDataDao: UserDataDao){

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val  day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {_, year, monthOfYear, dayOfMonth ->
                val date = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                val weight = binding.etWeightDaily.text.toString()
                val calories = binding.etCalories.text.toString()
                val steps = binding.etSteps.text.toString()

                //check if fields is not empty
                if (weight.isNotEmpty() && calories.isNotEmpty() && steps.isNotEmpty()){
                    lifecycleScope.launch{
                        userDataDao.insert(UserDataEntity(
                            weight=weight.toFloat(),
                            calories = calories.toInt(),
                            steps = steps.toInt(),
                            date = date.toString(),
                            )
                        )
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
            },
            year,
            month,
            day
        )

        datePickerDialog.show() //show calendar

/*
        val weight = binding?.etWeightDaily?.text.toString()
        val calories = binding?.etCalories?.text.toString()
        val steps = binding?.etSteps?.text.toString()


        //check if fields is not empty
        if (weight.isNotEmpty() && calories.isNotEmpty() && steps.isNotEmpty()){
            lifecycleScope.launch{
                userDataDao.insert(UserDataEntity(

                                                  weight=weight.toFloat(),
                                                  calories = calories.toInt(),
                                                  steps = steps.toInt(),

                )
                  )
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
        }*/
    }

    private fun setupListOfData(dataList: ArrayList<UserDataEntity>,
    userDataDao: UserDataDao){
               if (dataList.isNotEmpty()){
                  val dataAdapter = DataAdapter(dataList, {updateId ->
                      updateDataList(updateId, userDataDao)
                  }){deleteId ->
                      lifecycleScope.launch {
                          userDataDao.fetchDataById(deleteId).collect{
                              if (it != null){
                                  deleteDataAlertDialog(deleteId,userDataDao, it)
                              }
                          }
                      }
                  }
                   //set the layoutManager to use recyclerView
                   binding?.rvDataList?.layoutManager = LinearLayoutManager(requireContext())
                   binding?.rvDataList?.adapter = dataAdapter //adapter instance
                   binding?.rvDataList?.visibility = View.VISIBLE
                   binding?.tvNoDataAvailable?.visibility = View.GONE
                   binding.tvColumnDate?.visibility = View.VISIBLE
                   binding?.tvColumnWeight?.visibility = View.VISIBLE
                   binding?.tvColumnCalories?.visibility = View.VISIBLE
                   binding?.tvColumnSteps?.visibility = View.VISIBLE
               }else{
                   binding?.rvDataList?.visibility = View.GONE
                   binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                   binding.tvColumnDate?.visibility = View.GONE
                   binding?.tvColumnWeight?.visibility = View.GONE
                   binding?.tvColumnCalories?.visibility = View.GONE
                   binding?.tvColumnSteps?.visibility = View.GONE
               }
    }

    fun updateDataList(id: Int, userDataDao: UserDataDao){
        val updateData = Dialog(requireContext(),R.style.Theme_Dialog)
        updateData.setCancelable(false)
        val binding = UpdateDataBinding.inflate(layoutInflater)
        updateData.setContentView(binding.root)

        //launch a coroutine block to fetch the selected data and update it
        lifecycleScope.launch {
            userDataDao.fetchDataById(id).collect{
                if (it != null){
                    binding.etDate.setText(it.date)
                    binding.etUpdateWeight.setText(it.weight.toString())
                    binding.etUpdateCalories.setText(it.calories.toString())
                    binding.etUpdateSteps.setText(it.steps.toString())

                }
            }
        }

        binding.tvUpdate.setOnClickListener{

            val date = binding.etDate.text.toString()
            val weight = binding.etUpdateWeight.text.toString()
            val calories = binding.etUpdateCalories.text.toString()
            val steps = binding.etUpdateSteps.text.toString()

            if( weight.isNotEmpty() && calories.isNotEmpty() && steps.isNotEmpty() && date.isNotEmpty()){
                lifecycleScope.launch {
                    userDataDao.update(UserDataEntity(id, weight.toFloat(), calories.toInt(), steps.toInt(), date))
                    Toast.makeText(requireContext(), "Data Updated", Toast.LENGTH_LONG).show()
                    updateData.dismiss() //dismiss update dialog
                }
            }else{
                Toast.makeText(requireContext(), "Fields cannot be blank!", Toast.LENGTH_LONG).show()
            }
            }
        binding.tvCancel.setOnClickListener{
            updateData.dismiss()
        }
            updateData.show()  //start the dialog and display it on screen
    }

    //funktion to show Alert Dialog and delete selected data
    fun deleteDataAlertDialog(id:Int, userDataDao: UserDataDao, userData: UserDataEntity){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Data.") //title for alert
        builder.setMessage("Are you sure you want to delete this data?") //message for alert
        builder.setIcon(android.R.drawable.ic_dialog_alert)

         //performing positive button
        builder.setPositiveButton("Yes"){dialogInterface, _ ->
            lifecycleScope.launch {
                userDataDao.delete(UserDataEntity(id))
                Toast.makeText(
                    requireContext(), "Data deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                dialogInterface.dismiss() //dialog will be dismissed
            }
        }
          //performing negative button
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        //create alert dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show() //show the dialog

        }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
