
package com.example.carcommander
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response

//interface ApiService {
//    @POST("/user/signup")
//    fun postRequest(@Body body: Map<String, String>): Call<ResponseBody>
//}

data class UserRegistrationData(
    val first_name: String,
    val second_name: String,
    val username: String,
    val password: String,
    val email: String,
    val phone: String
)

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_window)

        //logic for button with text "back" - brings back to main activity
        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //logic for button with text "sign up"
        val signUpButton = findViewById<Button>(R.id.button_register_final)
        signUpButton.setOnClickListener {
            val registrationData = UserRegistrationData(
                first_name = findViewById<EditText>(R.id.edit_text_firstname).getText().toString(),
                second_name = findViewById<EditText>(R.id.edit_text_surname).getText().toString(),
                username = findViewById<EditText>(R.id.edit_text_username).getText().toString(),
                password = findViewById<EditText>(R.id.edit_text_password).getText().toString(),
                email = findViewById<EditText>(R.id.edit_text_email).getText().toString(),
                phone = findViewById<EditText>(R.id.edit_text_phone_number).getText().toString()
            )

            val requestBody = mapOf(
                "first_name" to registrationData.first_name,
                "second_name" to registrationData.second_name,
                "username" to registrationData.username,
                "password" to registrationData.password,
                "email" to registrationData.email,
                "phone" to registrationData.phone
            )
            signUp(requestBody)
        }
    }

    private fun signUp(requestBody: Map<String, String>) {
        //val firstName = findViewById<EditText>(R.id.edit_text_firstname).getText().toString()
        //val surName = findViewById<EditText>(R.id.edit_text_surname).getText().toString()
        //val username = findViewById<EditText>(R.id.edit_text_username).getText().toString()
        //val password = findViewById<EditText>(R.id.edit_text_password).getText().toString()
        //val email = findViewById<EditText>(R.id.edit_text_email).getText().toString()
        //val phoneNumber = findViewById<EditText>(R.id.edit_text_phone_number).getText().toString()

        //MAKING CONNECTION TO API
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


        //SENDING POST REQUEST TO THE API SERVER
        apiService.postRequestRegister(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Registration not getting response: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
