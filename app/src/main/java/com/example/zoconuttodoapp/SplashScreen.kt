package com.example.zoconuttodoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.zoconuttodoapp.Main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        Handler(Looper.getMainLooper()).postDelayed({

//            if (user != null){
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }else{
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()


        }, 2000)
    }
}