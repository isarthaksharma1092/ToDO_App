package com.isarthaksharma.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.isarthaksharma.todo.databinding.ActivityRegisterAuthBinding
import com.google.firebase.firestore.FirebaseFirestore
class registerAuth : AppCompatActivity() {
    //register and authorising
    private lateinit var auth: FirebaseAuth
    //dataBinding
    private lateinit var binding:ActivityRegisterAuthBinding
    //for sharing user info with Cloud firestore (firestore database)
    private lateinit var databaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //data binding
        binding = ActivityRegisterAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //register and authorising
        auth = FirebaseAuth.getInstance()

        //instance for Cloud firestore (database Firestore)
        databaseFirestore = FirebaseFirestore.getInstance()

        //#main
        binding.registerLoginBtn.setOnClickListener{
            if(checking()){
                val regName = binding.regNameInput.text.toString()
                val regEmail = binding.regEmailInput.text.toString()
                val regPhoneno = binding.regPhoneInput.text.toString()
                val regPassword = binding.regPasswordInput.text.toString()

                //creating hashmap for all above data [for database in firestore]
                //** no need to store password its for authentication **
                val user = hashMapOf(
                    "Name" to regName,
                    "Email" to regEmail,
                    "Phone" to regPhoneno
                )
                val Users = databaseFirestore.collection("USERS")
                val query = Users.whereEqualTo("Email",regEmail).get()
                    .addOnSuccessListener {task->
                        // if new user is accessing
                        // ** here we will register the user as well as add the data to the FIREBASE Database also
                        if(task.isEmpty)
                        {
                            auth.createUserWithEmailAndPassword(regEmail,regPassword).addOnCompleteListener(this){
                                    task->
                                if(task.isSuccessful)
                                {
                                    Users.document(regEmail).set(user)
                                    Toast.makeText(this,"Welcome 😁",Toast.LENGTH_LONG).show()

                                    //-------------------------------------------------------------------------------
                                    //sharing the email rest values we will get from fireStore
                                    val intent = Intent(this,MainActivity::class.java).apply {
                                        putExtra("email",regEmail)
                                    }
                                    startActivity(intent)
                                    finish()
                                    //-------------------------------------------------------------------------------
                                }
                                else{
                                    Toast.makeText(this,"Oppss Something went wrong\n Please try again",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        // if same email is used again
                        else{
                            Toast.makeText(this,"User already exists!\n Try Login instead",Toast.LENGTH_LONG).show()

                            // to delay the going to next activity by 2 second
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this,loginAuth::class.java)
                                startActivity(intent)
                            }, 1000)
                        }
                    }
            }
            else{
                Toast.makeText(this,"Please specify all details",Toast.LENGTH_LONG).show()
            }
        }
    }

    // trim{it<=' '} this will remove spaces entered by the user if he tries to play smart 😊
    private fun checking(): Boolean {
        if(binding.regNameInput.text.trim{it<=' '}.toString().isNotBlank() &&
            binding.regEmailInput.text.trim{it<=' '}.toString().isNotEmpty() &&
            binding.regPhoneInput.text.trim{it<=' '}.toString().isNotEmpty() &&
            binding.regPasswordInput.text.trim{it<=' '}.toString().isNotEmpty())
        {
            return true
        }
        return false
    }
}