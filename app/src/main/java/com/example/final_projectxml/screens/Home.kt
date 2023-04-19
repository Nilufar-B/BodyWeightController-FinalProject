package com.example.final_projectxml.screens

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_projectxml.R
import com.example.final_projectxml.database.*
import com.example.final_projectxml.databinding.FragmentHomeBinding
import com.example.final_projectxml.databinding.UpdateDataBinding
import kotlinx.coroutines.flow.collect
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
        lifecycleScope.launch {
            userDataDao.fetchAllData().collect{
                val list = ArrayList(it)
                setupListOfData(list, userDataDao)
            }
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

                   binding?.rvDataList?.layoutManager = LinearLayoutManager(requireContext())
                   binding?.rvDataList?.adapter = dataAdapter
                   binding?.rvDataList?.visibility = View.VISIBLE
                   binding?.tvNoDataAvailable?.visibility = View.GONE
               }else{
                   binding?.rvDataList?.visibility = View.GONE
                   binding?.tvNoDataAvailable?.visibility = View.VISIBLE
               }
    }

    fun updateDataList(id: Int, userDataDao: UserDataDao){
        val updateData = Dialog(requireContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog)
        updateData.setCancelable(false)
        val binding = UpdateDataBinding.inflate(layoutInflater)
        updateData.setContentView(binding.root)

        lifecycleScope.launch {
            userDataDao.fetchDataById(id).collect{
                if (it != null){
                    binding.etUpdateWeight.setText(it.weight.toString())
                    binding.etUpdateCalories.setText(it.calories.toString())
                    binding.etUpdateSteps.setText(it.steps.toString())
                }
            }
        }

        binding.tvUpdate.setOnClickListener{
            val weight = binding.etUpdateWeight.text.toString()
            val calories = binding.etUpdateCalories.text.toString()
            val steps = binding.etUpdateSteps.text.toString()

            if(weight.isNotEmpty() && calories.isNotEmpty() && steps.isNotEmpty()){
                lifecycleScope.launch {
                    userDataDao.update(UserDataEntity(id, weight.toFloat(), calories.toInt(), steps.toInt()))
                    Toast.makeText(requireContext(), "Data Updated", Toast.LENGTH_LONG).show()
                    updateData.dismiss()
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

    fun deleteDataAlertDialog(id:Int, userDataDao: UserDataDao, userData: UserDataEntity){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Data.") //title for alert
        builder.setMessage("Are you sure you want to delete this data?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

         //performing positive action
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
          //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        //create alert dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show() //show the dialog

        }


    }
