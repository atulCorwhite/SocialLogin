package com.example.sociallogin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

class FaceLogIn : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager
    private val EMAIL = "email"
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_log_in)
        FacebookSdk.sdkInitialize(applicationContext)
      //  AppEventsLogger.activateApp(this)
        val loginButton = findViewById<LoginButton>(R.id.loginButton)
        loginButton.setOnClickListener {
            loginButton.setReadPermissions(listOf(EMAIL))
            callbackManager = CallbackManager.Factory.create()
            // If you are using in a fragment, call loginButton.setFragment(this);
            // Callback registration
            // If you are using in a fragment, call loginButton.setFragment(this);
            // Callback registration
            loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("MainActivity", "Facebook token: " + loginResult!!.accessToken.token)
                    startActivity(
                        Intent(
                            applicationContext,
                            ShareDataToSocial::class.java
                        )
                    )// App code
                }
                override fun onCancel() { // App code
                }
                override fun onError(exception: FacebookException) { // App code
                }
            })
            callbackManager = CallbackManager.Factory.create()
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(callbackManager,object :FacebookCallback<LoginResult>
            {
                override fun onSuccess(result: LoginResult) {
                    TODO("Not yet implemented")
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException) {
                    TODO("Not yet implemented")
                }
            }
            )

        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}