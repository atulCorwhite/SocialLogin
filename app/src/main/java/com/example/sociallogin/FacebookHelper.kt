package com.example.sociallogin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.model.*
import com.facebook.share.widget.ShareDialog
import org.json.JSONObject
import java.util.*


class FacebookHelper /*( activity: Activity, fbSignInListener: OnFbSignInListener)*/ {
    private val permissions: Collection<String> =
        Arrays.asList("public_profile ", "email")
    private var callbackManager: CallbackManager? = null
    private var shareDialog: ShareDialog? = null
    private var activity: Activity? = null
    private val fragment: Fragment? = null
    private var fbSignInListener: OnFbSignInListener? = null

    private var loginManager: LoginManager? = null

    private val URL = "https://corp.dewsolutions.in/lms/symfony/web/index.php/leave/applyLeave"

    private  var image: Bitmap?=null
    private  var   videoFileUri : Uri=Uri.parse("https://www.youtube.com/watch?v=S7c15G5g6m8")

    /*   init {
           this.activity = activity
           this.fbSignInListener = fbSignInListener
       }*/


    constructor( activity: Activity, fbSignInListener: OnFbSignInListener) {
        this.activity = activity
        this.fbSignInListener = fbSignInListener
    }
    constructor( activity: Activity) {
        this.activity = activity
        shareDialog = ShareDialog(activity)

    }

    open fun connect() {
        //Mention the GoogleSignInOptions to get the user profile and email.
        // Instantiate Google SignIn Client.
        callbackManager = create()
        loginManager = LoginManager.getInstance()
        if (activity != null) {
            loginManager!!.logInWithReadPermissions(activity!!, permissions)
        } else {
          /*  loginManager!!.logInWithReadPermissions(fragment!!, permissions)
            loginManager!!.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        if (loginResult != null) {
                            Log.e("data","dataaa")
                            callGraphAPI(loginResult.accessToken)
                        }
                    }

                    override fun onCancel() {
                        Log.e("data","dataaa")
                    }

                    override fun onError(exception: FacebookException) {
                        Log.e("data","dataaa")

                    }
                })*/






            ////new code


            loginManager!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.e("datraa","FaceBookHelper")
                    callGraphAPI(result.accessToken)
                    /*   val intent = Intent(this@MainActivity, ShareDataToSocial::class.java)
                       intent.putExtra("user_name", "")
                       intent.putExtra("user_email", "")*/
                   // startActivity(intent)
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
    }

    private fun callGraphAPI(accessToken: AccessToken) {
        Log.e("Data","dataaaaaaaaaaaaa")
        val request = GraphRequest.newMeRequest(accessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                    fbSignInListener!!.OnFbSignInComplete(response, null)
                }
            })

        val parameters = Bundle()
        //Explicitly we need to specify the fields to get values else some values will be null.
        parameters.putString("fields", "id,email,first_name,gender,last_name,link,name")
        request.parameters = parameters
        request.executeAsync()

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (callbackManager != null) {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun shareOnFBWall(title: String?, description: String?, url: Uri) {
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent: ShareLinkContent = ShareLinkContent.Builder()
                .setContentUrl(url)
                .setQuote("sharing")
                .build()
            shareDialog!!.show(linkContent)

            /*   val content: ShareLinkContent = ShareLinkContent.Builder()
                   .setContentUrl(Uri.parse("https://developers.facebook.com"))
                   .build()
   */

            //share pic
/*           val  photo:SharePhoto =  SharePhoto.Builder()
                .setBitmap(image)
                .build();
            val photoContent: SharePhotoContent =  SharePhotoContent.Builder()
                .addPhoto(photo)
                .build()*/
            //share video

            /*      val video: ShareVideo =ShareVideo.Builder()
                      .setLocalUrl(videoFileUri)
                      .build()
                  val content: ShareVideoContent =ShareVideoContent.Builder()
                      .setVideo(video)
                      .build()

                  shareDialog!!.show(content, ShareDialog.Mode.AUTOMATIC)*/


            /*    val sharePhoto1 :SharePhoto=  SharePhoto.Builder()
                .setBitmap(image)
            .build();
            val sharePhoto2 :SharePhoto=  SharePhoto.Builder()
                .setBitmap(image)
                .build();
             val shareVideo1 :ShareVideo =  ShareVideo.Builder()
                .setLocalUrl(Uri.parse("https://www.youtube.com/watch?v=S7c15G5g6m8"))
            .build();
            val shareVideo2 :ShareVideo =  ShareVideo.Builder()
                .setLocalUrl(Uri.parse("https://www.youtube.com/watch?v=S7c15G5g6m8"))
                .build();

              val shareContent =  ShareMediaContent.Builder()
                .addMedium(shareVideo1)
                .addMedium(shareVideo2)
                .addMedium(sharePhoto1)
                .addMedium(sharePhoto2)
               .setContentUrl(Uri.parse("https://img.tatacliq.com/images/i8/437Wx649H/MP000000013167257_437Wx649H_202205180820571.jpeg"))
               .build();

            shareDialog!!.show(shareContent, ShareDialog.Mode.AUTOMATIC);*/

        }
    }
}

/**
 * Interface to listen the Facebook login
 */
interface OnFbSignInListener {
    fun OnFbSignInComplete(graphResponse: GraphResponse?, error: String?)
}
