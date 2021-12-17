package com.example.pocxiafra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.emailTextEdit
import kotlinx.android.synthetic.main.activity_register.passwordEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setup()
        addListeners()
    }

    private fun setup(){
        auth = Firebase.auth
    }

    private fun addListeners(){
        signinButton.setOnClickListener{
            signinButton.isClickable = false
            signinButton.isEnabled = false
            if(emailTextEdit.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                auth.signInWithEmailAndPassword(emailTextEdit.text.toString(), passwordEditText.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            completeLogin()
                        }
                        else{
                            failedLogin()
                        }
                    }
            }
        }
    }

    private fun completeLogin(){
        Toast.makeText(this, "session successfully started", Toast.LENGTH_SHORT).show()
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
        this.finish()
    }

    private fun failedLogin(){
        AlertDialog.Builder(this).
        setTitle("Error")
            .setMessage("Incorrect session, try again")
            .create()
            .show()
    }
}