package com.example.sociallogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import org.json.JSONException
import java.util.*


class MainActivity : AppCompatActivity(),GoogleSignInHelper.OnGoogleSignInListener,OnFbSignInListener {
   lateinit var  progressBar:ProgressBar
   //Google plus sign-in button
    private var googleSignInHelper: GoogleSignInHelper? = null
    lateinit var gSignInButton: Button
    private var isFbLogin = false
    lateinit var context:Context

    // facebook///:::::::::::::::
    private var fbConnectHelper: FacebookHelper? = null
    lateinit var fbSignInButton: Button
    lateinit var  loginButton:LoginButton

    private val TAG = MainActivity::class.java.simpleName
    private val URL = "https://github.com/rajivmanivannan/Android-Social-Login"

    private var callbackManager: CallbackManager? = null
    private val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context=this;
        FacebookSdk.sdkInitialize(context);

        progressBar = findViewById(R.id.progress_bar)
        gSignInButton = findViewById(R.id.main_g_sign_in_button)

        //----------------------------------Google +Sign in-----------------------------------//
        googleSignInHelper = GoogleSignInHelper(this, this)
        googleSignInHelper!!.connect()

        gSignInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            googleSignInHelper!!.signIn()
            isFbLogin = false
        }


        //--------------------------------Facebook login--------------------------------------//
        callbackManager = CallbackManager.Factory.create()
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        KeyHashGenerator.generateKey(this)
        fbConnectHelper = FacebookHelper(this, this)
        fbSignInButton = findViewById(R.id.fb_sign_in_button)
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val intent = Intent(this@MainActivity, ShareDataToSocial::class.java)
                intent.putExtra("user_name", "User Id:-"+result.accessToken.userId)
                intent.putExtra("user_email", "")
                startActivity(intent)
            }
            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.e("datraa","datraaError")
            }
        })

    }

    override fun onStart() {
        super.onStart()
        googleSignInHelper!!.onStart()
    }
    override fun OnGSignInSuccess(googleSignInAccount: GoogleSignInAccount?) {
        progressBar.visibility = View.GONE
        if (googleSignInAccount != null) {
            Log.e("name:>>>>>",googleSignInAccount.givenName + googleSignInAccount.photoUrl)
            val intent = Intent(this@MainActivity, ShareDataToSocial::class.java)
            intent.putExtra("user_name", googleSignInAccount.givenName)
            intent.putExtra("user_email", googleSignInAccount.email)
            startActivity(intent)

        }
    }

    override fun OnGSignInError(error: String?) {

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignInHelper?.onActivityResult(requestCode, resultCode, data)
        fbConnectHelper!!.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(
            requestCode,
            resultCode,
            data);
    }

    override fun OnFbSignInComplete(graphResponse: GraphResponse?, error: String?) {
        progressBar.visibility = View.GONE
        if (error == null) {
            try {
                val jsonObject = graphResponse!!.getJSONObject()
              /*  userName.text = jsonObject!!.getString("name")
                email.text = jsonObject!!.getString("email")*/
                val id = jsonObject!!.getString("id")
                val profileImg = "http://graph.facebook.com/$id/picture?type=large"
            } catch (e: JSONException) {
                Log.i(TAG, e.message!!)
            }
        }
    }
}