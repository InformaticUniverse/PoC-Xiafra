package com.example.pocxiafra

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.emailTextEdit
import kotlinx.android.synthetic.main.activity_register.passwordEditText

class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var auth : FirebaseAuth
    var name = ""
    var email = ""
    var career = 0
    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        addListeners()
        setup()
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
    }

    private fun setup(){
        title = "Register"
        ArrayAdapter.createFromResource(this, R.array.careers_array, android.R.layout.simple_spinner_dropdown_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                careerSpinner.adapter = adapter
            }
        careerSpinner.onItemSelectedListener = this
    }


    private fun addListeners(){
        signupButton.setOnClickListener{
            if(nameEditText.text.isNotEmpty() && emailTextEdit.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                signupButton.isClickable = false
                signupButton.isEnabled = false
                auth.createUserWithEmailAndPassword(emailTextEdit.text.toString(), passwordEditText.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            completeRegister()
                        }
                        else{
                            Log.w("Error", "createUserWithEmail:failure", task.exception)
                            failedRegister()

                        }
                    }
            }
        }

        nameEditText.setOnFocusChangeListener{ view, b ->
            if(!b){
                checkForm()
                nameAvise.isVisible = !nameEditText.text.isNotEmpty()
                name = nameEditText.text.toString()
            }
        }

        emailTextEdit.setOnFocusChangeListener{ view, b ->
            if(!b){
                checkForm()
                emailAvise.isVisible = !EMAIL_REGEX.toRegex().matches(emailTextEdit.text.toString())
                email = emailTextEdit.text.toString()
            }
        }
        passwordEditText.setOnFocusChangeListener{ view, b ->
            if(!b){
                checkForm()
                passwordAvise.isVisible = !passwordEditText.text.isNotEmpty()
            }
        }
    }

    private fun checkForm(){

        if( nameEditText.text.isNotEmpty() &&
            EMAIL_REGEX.toRegex().matches(emailTextEdit.text.toString()) &&
            passwordEditText.text.isNotEmpty() &&
            career > 0){
                signupButton.isEnabled = true
                signupButton.isClickable = true
        }else{
            signupButton.isEnabled = false
            signupButton.isClickable = false
        }
    }

    private fun completeRegister(){
        val user = auth.currentUser
        val uid = user!!.uid
        val map = hashMapOf(
            "name" to name,
            "email" to email,
            "career" to career
        )

        val db = Firebase.firestore

        db.collection("users").document(uid).set(map)
            .addOnSuccessListener {
            Toast.makeText(this, "Registration completed successfully", Toast.LENGTH_SHORT).show()
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            this.finish()
        }

    }

    private fun failedRegister(){
        AlertDialog.Builder(this).
            setTitle("Error")
            .setMessage("Registration failed, try again")
            .create()
            .show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        career = pos
        checkForm()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}