package com.example.pocxiafra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 89
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }


    private fun setup() {
        auth = Firebase.auth
        addListeners()
    }

    private fun addListeners(){
        registerButton.setOnClickListener{
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        loginButton.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        googleButton.setOnClickListener{
            goToGoogleSigin()
        }
    }

    override fun onStart() {
        super.onStart()
        checkUserIsLoggedIn()
    }



    private fun goToGoogleSigin(){

        val googleIntent = Intent(this, LoginWithGoogleActivity::class.java)
        startActivity(googleIntent)
    }

    private fun failedGoogleLogin(){
        AlertDialog.Builder(this).
        setTitle("Error")
            .setMessage("An error has ocurred , try again")
            .create()
            .show()
    }

    private fun checkUserIsLoggedIn(){
        val currentUser = auth.currentUser
        if(currentUser != null){
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            this.finish()
        }
    }
}