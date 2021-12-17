package com.example.pocxiafra

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chemistry.*
import kotlinx.android.synthetic.main.activity_systems.*

class HomeActivity : AppCompatActivity(), TextWatcher{
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup(){
        auth = Firebase.auth
        var currentUser = auth.currentUser
        var db = Firebase.firestore
        var userCareer = 0L
        if(currentUser != null){
            db.collection("users").document(currentUser.uid).get().addOnSuccessListener {
                userCareer = it.get("career") as Long
                println(userCareer)
                if(userCareer.compareTo(1) == 0){
                    setContentView(R.layout.activity_systems)
                    addSystemsListeners()
                }else if(userCareer.compareTo(2) == 0){
                    setContentView(R.layout.activity_chemistry)
                    addchemistryListeners()
                }
            }
        }
    }

    private fun addSystemsListeners(){

        button1.setOnClickListener{
            AlertDialog.Builder(this)
                .setMessage("ISC")
                .create()
                .show()
        }

        button2.setOnClickListener{
            label1.text = fieldOfText1.text
        }

    }

    private fun addchemistryListeners(){
        chemsButton.setOnClickListener{
            AlertDialog.Builder(this)
                .setMessage("Ing.Quimica " + chemFieldOfText1.text.toString())
                .create()
                .show()
        }
        chemFieldOfText2.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        chemLabel1.text = p0
    }

    override fun afterTextChanged(p0: Editable?) {

    }


}