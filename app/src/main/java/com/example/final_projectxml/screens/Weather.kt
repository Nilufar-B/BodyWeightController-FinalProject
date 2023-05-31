package com.example.final_projectxml.screens


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.final_projectxml.DialogManager
import com.example.final_projectxml.databinding.FragmentWeatherBinding
import com.example.final_projectxml.weatherApi.ViewModel
import com.example.final_projectxml.weatherApi.WeatherModel
import com.example.final_projectxml.weatherApi.isPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.squareup.picasso.Picasso
import org.json.JSONObject


const val API_KEY = "fc9e843259334c1f8b9164935232905"

class Weather : Fragment() {

    private lateinit var fLocationClient: FusedLocationProviderClient
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private val model: ViewModel by activityViewModels()

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        locationClient()
        updateCurrentCard()
        //requestWeatherData("Stockholm")
       // getLocation()
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun locationClient() = with(binding){
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        btnSync.setOnClickListener{
          checkLocation()
            //getLocation()
        }
    }


    private fun checkLocation(){
        if (isLocationEnabled()){
            getLocation()
        }else{
            DialogManager.locationSettingsDialog(requireContext(), object : DialogManager.Listener{
                override fun onClick(){
                  startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun isLocationEnabled(): Boolean{
        val lManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation(){
        val ct = CancellationTokenSource()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener{
                requestWeatherData("${it.result.latitude},${it.result.longitude}")
            }
    }

    private fun updateCurrentCard() = with(binding){
     model.liveDataCurrent.observe(viewLifecycleOwner){
         val temp = "${it.currentTemp.toFloat().toInt()}°C"
         tvDateWeather.text = it.date
         tvCity.text = it.cityName
         tvTemp.text = temp
         tvCondition.text = it.condition
         Picasso.get().load("https:" + it.imageUrl).into(imWeather)

         val temperature = it.currentTemp.toFloat()

         if (temperature < 10){
             tvAdviceByTemp.text = "In such weather, cardio in the gym or at home will help increase activity."
         }
         else if (temperature > 10 && temperature < 30){
             tvAdviceByTemp.text = "Walking in such wonderful weather only increases activity. Movement is life!"
         }
         else if (temperature >= 30){
             tvAdviceByTemp.text = "Finally you can swim and increase your activity!"
         }
        }
    }

private fun permissionListener(){
    pLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        Toast.makeText(requireContext(), "Permission is $it", Toast.LENGTH_LONG).show()
    }
}

    private fun checkPermission(){
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun requestWeatherData(cityName: String){
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                cityName +
                "&days=" +
                "1" +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result -> parseWeatherData(result)
            },
            {
                error-> println("Volley error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseWeatherData(result: String){
       val mainObject = JSONObject(result)
       parseCurrentData(mainObject)

    }

    private fun parseCurrentData(mainObject: JSONObject)   {
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getJSONObject("condition")
                .getString("text"),
            mainObject.getJSONObject("current").getString("temp_c"),
            mainObject.getJSONObject("current").getJSONObject("condition")
                .getString("icon")
        )
        model.liveDataCurrent.value = item
    }
    companion object {
        @JvmStatic
        fun newInstance() = Weather()
    }
}
