package com.example.kotlin.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.kotlin.Lib
import com.example.kotlin.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginPageLinearLayout         : LinearLayout
    private lateinit var forgetPasswordPageLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val loginEmailAddressEditText: EditText = findViewById(R.id.login_email_address)
        val loginPasswordEditText    : EditText = findViewById(R.id.login_password)
        val loginButton              : Button   = findViewById(R.id.login)
        val forgetPasswordTextView   : TextView = findViewById(R.id.forget_password)
        val createAccountButton      : Button   = findViewById(R.id.create_account)
        // 忘記密碼
        val findEmailAddressEditText : EditText = findViewById(R.id.find_email_address)
        val findAccountButton        : Button   = findViewById(R.id.find_account)

        loginPageLinearLayout          = findViewById(R.id.login_page)
        forgetPasswordPageLinearLayout = findViewById(R.id.forget_password_page)

        loginEmailAddressEditText.addTextChangedListener {
            if (loginEmailAddressEditText.text.toString() != "" && loginPasswordEditText.text.toString() != "") {
                loginButton.isEnabled          = true
                loginButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#166fe5"))
            } else {
                loginButton.isEnabled          = false
                loginButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b2dffc"))
            }
        }

        loginPasswordEditText.addTextChangedListener {
            if (loginEmailAddressEditText.text.toString() != "" && loginPasswordEditText.text.toString() != "") {
                loginButton.isEnabled          = true
                loginButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#166fe5"))
            } else {
                loginButton.isEnabled          = false
                loginButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b2dffc"))
            }
        }

        loginButton.setOnClickListener {
            Lib.hideKeyboard(it)

            login(loginEmailAddressEditText.text.toString(), loginPasswordEditText.text.toString())
        }

        forgetPasswordTextView.setOnClickListener {
            loginPageLinearLayout.visibility          = View.GONE
            forgetPasswordPageLinearLayout.visibility = View.VISIBLE
        }

        createAccountButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)

            startActivity(intent)
        }

        findEmailAddressEditText.addTextChangedListener {
            if (findEmailAddressEditText.text.toString() != "") {
                findAccountButton.isEnabled          = true
                findAccountButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#166fe5"))
            } else {
                findAccountButton.isEnabled          = false
                findAccountButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b2dffc"))
            }
        }

        findAccountButton.setOnClickListener {
            Lib.hideKeyboard(it)

            findAccount(findEmailAddressEditText.text.toString())
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && loginPageLinearLayout.visibility == View.GONE) {
            forgetPasswordPageLinearLayout.visibility = View.GONE
            loginPageLinearLayout.visibility          = View.VISIBLE
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun login(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "登入成功！", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()
            } else {
                if ("A network error" in it.exception.toString()) {
                    Lib.useAlertDialog(this, "沒有網路連線", "請檢查網路連線後再試一次。")
                } else if ("badly formatted" in it.exception.toString()) {
                    Lib.useAlertDialog(this, "錯誤", "電子郵件格式錯誤！")
                } else {
                    Lib.useAlertDialog(this, "密碼錯誤", "您輸入的密碼不正確，請再試一次。")
                }
            }
        }
    }

    private fun findAccount(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                AlertDialog.Builder(this)
                    .setMessage("已發送電子郵件！")
                    .setPositiveButton("確定", backLogin)
                    .show()
            } else {
                if ("A network error" in it.exception.toString()) {
                    Lib.useAlertDialog(this, "沒有網路連線", "請檢查網路連線後再試一次。")
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val backLogin = DialogInterface.OnClickListener { _, _ ->
        forgetPasswordPageLinearLayout.visibility = View.GONE
        loginPageLinearLayout.visibility          = View.VISIBLE
    }
}