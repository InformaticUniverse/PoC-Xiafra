package com.example.pocxiafra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login_with_google.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginWithGoogleActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var auth : FirebaseAuth
    var career = 0
    private val GOOGLE_SIGN_IN = 89
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_google)


        setup()
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
    }

    private fun setup(){
        title = "Google Sign In"


        ArrayAdapter.createFromResource(this, R.array.careers_array, android.R.layout.simple_spinner_dropdown_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                careerGoogle.adapter = adapter
            }
        careerGoogle.onItemSelectedListener = this

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        val googleClient = GoogleSignIn.getClient(this, gso)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        signinGoogle.setOnClickListener{
            goToHome()
            signinGoogle.isClickable = false
            signinGoogle.isEnabled = false
        }


    }

    private fun checkForm(){

        if( googleName.text.isNotEmpty() &&
            career > 0){
            signinGoogle.isEnabled = true
            signinGoogle.isClickable = true
        }else{
            signinGoogle.isEnabled = false
            signinGoogle.isClickable = false
        }
    }

    private fun completeLogin(credential: AuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, "session successfully started, please fill in the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                failedGoogleLogin()
            }
        }

    }

    private fun goToHome(){

        val user = auth.currentUser
        val map = hashMapOf(
            "name" to googleName.text.toString(),
            "career" to career
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(user!!.uid).set(map)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration completed successfully", Toast.LENGTH_SHORT).show()
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                this.finish()
            }
            .addOnFailureListener{ e ->
                Log.w("ERRRRR", "Error adding document", e)
                AlertDialog.Builder(this).
                setTitle("Error")
                    .setMessage("failed to save data, try again")
                    .create()
                    .show()
            }

    }

    private fun failedGoogleLogin(){
        AlertDialog.Builder(this).
        setTitle("Error")
            .setMessage("Incorrect session, try again")
            .create()
            .show()
        onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                var account = task.getResult(ApiException::class.java)

                if(account != null){
                    var credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    completeLogin(credential)
                }
            }catch (e: ApiException){
                failedGoogleLogin()
            }

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        career = pos
        checkForm()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}