package com.isarthaksharma.todo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.api.ContextRule
import com.google.firebase.firestore.FirebaseFirestore
import com.isarthaksharma.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //shared Preferences
        val sharedPref= this.getPreferences(Context.MODE_PRIVATE) ?:return
        val isloggedin = sharedPref.getString("Email","1")

        // 1 means app is being launched for the first time or user logged out
        if(isloggedin == "1")
        {
            val email = intent.getStringExtra("email")
            if(email!=null){
                setInfo(email)
                with(sharedPref.edit())
                {
                    putString("Email",email)
                    apply()
                }
            }
            else{
                var intent = Intent(this,loginAuth::class.java)
                startActivity(intent)
                finish()
            }
        }
        // else user is logged in already
        else{
            setInfo(isloggedin)
        }

        //------------------------------------------------------------------------------------------
        //Logout Button
        //1) first delete the preferences and
        //2) go to login page
        binding.Logout.setOnClickListener{
            sharedPref.edit().remove("Email").apply()

            var intent = Intent(this,loginAuth::class.java)
            startActivity(intent)
            finish()
        }
        //------------------------------------------------------------------------------------------
    }

    private fun setInfo(email:String?) {
        db = FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener {
                        tasks->
                    binding.name.text = "Name: ${tasks.get("Name").toString()}"
                    binding.email.text = "Email Address: ${tasks.get("Email").toString()}"
                    binding.phone.text = "Phone Number: ${tasks.get("Phone").toString()}"
                }
        }
    }
}
