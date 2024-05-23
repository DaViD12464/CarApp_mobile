package com.example.carcommander

import android.media.Image
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import androidx.recyclerview.widget.RecyclerView
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Car(
    val imageUrl: String,
    val brand: String,
    val model: String
)
class ContentActivity : AppCompatActivity()
{
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        //API CONNECTION BELOW
        //==========================================================================================
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.14:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        fetchAllCars()

    }

    private fun fetchAllCars() {
        val call = apiService.getAllCars()
        call.enqueue(object : Callback<List<CarData>> {
            override fun onResponse(call: Call<List<CarData>>, response: Response<List<CarData>>) {
                if (response.isSuccessful) {
                    val carList = response.body()
                    carList?.let {

                        Log.d("ContentActivity", "Cars: $it")
                    }
                } else {

                    Log.e("ContentActivity", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<CarData>>, t: Throwable) {

                Log.e("ContentActivity", "Failure: ${t.message}")
            }
        })
    }
}