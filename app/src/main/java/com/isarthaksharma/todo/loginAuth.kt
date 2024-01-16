package com.isarthaksharma.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.isarthaksharma.todo.databinding.ActivityLoginAuthBinding

class loginAuth : AppCompatActivity() {
    //dataBinding
    private lateinit var binding: ActivityLoginAuthBinding
    //firebase authentication
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //data Binding
        binding = ActivityLoginAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newAccBtn.setOnClickListener{
            val intent = Intent(this,registerAuth::class.java)
            startActivity(intent)
        }


        //fireBase authentication
        auth = FirebaseAuth.getInstance()

        //collection activity information
        binding.LoginBtn.setOnClickListener{
            if(check()){
                val email = binding.EmailInput.text.toString()
                val password = binding.passwordInput.text.toString()

                //remember do the task or return a exception
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){ task  ->

                        if(task.isSuccessful) //user enter correct credentials
                        {
                            //-------------------------------------------------------------------------------
                            //sharing the email rest values we will get from fireStore
                            Toast.makeText(this,"Welcome Back 👋",Toast.LENGTH_LONG).show()
                            val intent = Intent(this,MainActivity::class.java).apply {
                                putExtra("email",email)
                            }
                            startActivity(intent)
                            finish()
                            //-------------------------------------------------------------------------------

                        }
                        else //user entered wrong credentials
                        {
                            Toast.makeText(this,"Please Recheck Your Email and Password!",Toast.LENGTH_LONG).show()
                        }
                }
            }
            else{
                Toast.makeText(this,"Something is off \n Enter the corrent details!",Toast.LENGTH_LONG).show()
            }
        }

    }

    // trim{it<=' '} this will remove spaces entered by the user if he tries to play smart 😊
    private fun check():Boolean
    {
        if(binding.EmailInput.text.trim{it<=' '}.toString().isNotBlank() && binding.passwordInput.text.trim{it<=' '}.toString().isNotEmpty()){
            return true
        }
        return false
    }
}

