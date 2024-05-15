package com.example.carcommander

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class UserLoginData(
    val usernameOrMail: String,
    val password: String
)

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_window)

        //logic for button with text "back" - brings back to main activity
        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.button_login_final)
        loginButton.setOnClickListener {

            val loginData = UserLoginData(
                usernameOrMail = findViewById<EditText>(R.id.edit_text_login_username).getText().toString(),
                password = findViewById<EditText>(R.id.edit_text_login_login_password).getText().toString()
            )

            val requestBody = mapOf(
                "usernameOrMail" to loginData.usernameOrMail,
                "password" to loginData.password
            )

            login(requestBody)


        }
    }

    private fun login(requestBody: Map<String, String>) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.14:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.postRequestLogin(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@LoginActivity, ContentActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Login not getting response: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}