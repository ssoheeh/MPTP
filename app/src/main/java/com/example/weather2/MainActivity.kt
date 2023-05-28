package com.example.weather2

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.weather2.databinding.ActivityMainBinding
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var city : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    fun init(){
        binding.getWeather.setOnClickListener {
            CurrentCall()
            binding.city.setText("")
        }


    }

    private fun CurrentCall() {
        city = binding.city.text.toString()
        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+"d74c3bbee7a3c497383271ff0d494542"

        val stringRequest = StringRequest(Request.Method.GET,url,
        Response.Listener<String>{
            response ->
            val jsonObject = JSONObject(response)

            val weatherJson = jsonObject.getJSONArray("weather")
            val weatherObj = weatherJson.getJSONObject(0)

            var weather = weatherObj.getString("description")
            val imgURL = "http://openweathermap.org/img/w/" + weatherObj.getString("icon") + ".png"
            Glide.with(this).load(imgURL).into(findViewById<ImageView>(R.id.weatherIcon))
            val tempK = JSONObject(jsonObject.getString("main"))
            val tempDo = (Math.round((tempK.getDouble("temp")-273.15)*100)/100.0)
            weather = weather + tempDo + "°C"
            binding.result.text = weather


        },
        Response.ErrorListener {
            binding.result.text = "error"
        })

        requestQueue.add(stringRequest)
    }
}