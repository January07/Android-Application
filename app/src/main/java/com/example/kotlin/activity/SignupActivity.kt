package com.example.kotlin.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.kotlin.Lib
import com.example.kotlin.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        val signupEmailAddressEditText : EditText     = findViewById(R.id.signup_email_address)
        val signupPasswordEditText     : EditText     = findViewById(R.id.signup_password)
        val signupNicknameEditText     : EditText     = findViewById(R.id.signup_nickname)
        val signupButton               : Button       = findViewById(R.id.signup)
        val returnLoginPageLinearLayout: LinearLayout = findViewById(R.id.return_login_page)

        signupEmailAddressEditText.addTextChangedListener {
            if (signupEmailAddressEditText.text.toString() != "" && signupPasswordEditText.text.toString() != "" && signupNicknameEditText.text.toString() != "") {
                signupButton.isEnabled          = true
                signupButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#166fe5"))
            } else {
                signupButton.isEnabled          = false
                signupButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b2dffc"))
            }
        }

        signupPasswordEditText.addTextChangedListener {
            if (signupEmailAddressEditText.text.toString() != "" && signupPasswordEditText.text.toString() != "" && signupNicknameEditText.text.toString() != "") {
                signupButton.isEnabled          = true
                signupButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#166fe5"))
            } else {
                signupButton.isEnabled          = false
                signupButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b2dffc"))
            }
        }

        signupNicknameEditText.addTextChangedListener {
            if (signupEmailAddressEditText.text.toString() != "" && signupPasswordEditText.text.toString() != "" && signupNicknameEditText.text.toString() != "") {
                signupButton.isEnabled          = true
                signupButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#166fe5"))
            } else {
                signupButton.isEnabled          = false
                signupButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b2dffc"))
            }
        }

        signupButton.setOnClickListener {
            Lib.hideKeyboard(it)

            if ("." in signupNicknameEditText.text.toString() || "#" in signupNicknameEditText.text.toString() || "$" in signupNicknameEditText.text.toString() || "[" in signupNicknameEditText.text.toString() || "]" in signupNicknameEditText.text.toString()) {
                Lib.useAlertDialog(this, "??????", "?????????????????????????????????")
            } else {
                signup(signupEmailAddressEditText.text.toString(), signupPasswordEditText.text.toString(), signupNicknameEditText.text.toString())
            }
        }

        returnLoginPageLinearLayout.setOnClickListener {
            finish()
        }
    }

    private fun signup(email: String, password: String, nickname: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show()

                // ?????? Email ?????????
                Firebase.auth.currentUser?.sendEmailVerification()

                // ?????? Firebase Realtime Database
                val myRef = FirebaseDatabase.getInstance().getReference("Users")
                val user  = User(email, password)

                myRef.child(nickname).setValue(user)

                val intent = Intent(this, MainActivity::class.java)

                // ?????? stack ?????? activity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                when {
                    "A network error" in it.exception.toString() -> {
                        Lib.useAlertDialog(this, "??????????????????", "???????????????????????????????????????")
                    }
                    "another account" in it.exception.toString() -> {
                        Lib.useAlertDialog(this, "??????", "????????????????????????????????????")
                    }
                    "badly formatted" in it.exception.toString() -> {
                        Lib.useAlertDialog(this, "??????", "??????????????????????????????????????????")
                    }
                    "given password " in it.exception.toString() -> {
                        Lib.useAlertDialog(this, "??????", "?????????????????? 6 ????????????")
                    }
                    else                                         -> {
                        Lib.useAlertDialog(this, "??????", it.exception.toString())
                    }
                }
            }
        }
    }

    data class User(val email: String, val password: String)
}