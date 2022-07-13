package com.example.sociallogin

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.ShareVideo
import com.facebook.share.model.ShareVideoContent
import com.facebook.share.widget.ShareDialog


class ShareDataToSocial : AppCompatActivity() {
    lateinit var fbShareButton: Button
    lateinit var main_name_txt: TextView
    lateinit var main_email_txt: TextView
    private val URL = "https://github.com/rajivmanivannan/Android-Social-Login"
    var REQUEST_TAKE_GALLERY_VIDEO: Int = 1
    var filemanagerstring: String = ""
    private var shareDialog: ShareDialog? = null
    private var callbackManager: CallbackManager? = null

    //Google plus sign-in button
    private var googleSignInHelper: GoogleSignInHelper? = null

    lateinit var builder:AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_data_to_social)

        fbShareButton = findViewById(R.id.main_fb_share_button)
        main_name_txt = findViewById(R.id.main_name_txt)
        main_email_txt = findViewById(R.id.main_email_txt)

        val userName: String = intent.getStringExtra("user_name").toString()
        val email: String = intent.getStringExtra("user_email").toString()

        main_name_txt.text = userName
        main_email_txt.text = email

        googleSignInHelper = GoogleSignInHelper(this)

        callbackManager = CallbackManager.Factory.create();
        shareDialog = ShareDialog(this)
         shareDialog!!.registerCallback(callbackManager!!,callback);

        fbShareButton.setOnClickListener {
            showDialog("FaceBook")

        }
    }
    private val callback: FacebookCallback<Sharer.Result> =
        object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result) {
                Toast.makeText(applicationContext,"Share data sucess",Toast.LENGTH_SHORT).show()
                builder.dismiss()
            }

            override fun onCancel() {
                Log.v("cancle", "Sharing cancelled")

                Toast.makeText(applicationContext,"Sharing cancelled",Toast.LENGTH_SHORT).show()
                // Write some code to do some operations when you cancel sharing content.
            }
            override fun onError(error: FacebookException) {
                Log.v("errror", error.message!!)
                Toast.makeText(applicationContext,"Sharing Error",Toast.LENGTH_SHORT).show()
                // Write some code to do some operations when some error occurs while sharing content.
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                var selectedImageUri: Uri = data?.data!!;
                val cr: ContentResolver = this.getContentResolver()
                val mime = cr.getType(selectedImageUri)
                Log.e("mime::-------", mime+"")
                if (mime.toString().contains("image")) {
                    val  photo: SharePhoto =  SharePhoto.Builder()
                        .setImageUrl(selectedImageUri)
                        .build();
                    val photoContent: SharePhotoContent =  SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build()
                    shareDialog!!.show(photoContent, ShareDialog.Mode.AUTOMATIC)

                } else  if (mime.toString().contains("video")) {
                    val video: ShareVideo = ShareVideo.Builder()
                        .setLocalUrl(selectedImageUri)
                        .build()
                    val content: ShareVideoContent = ShareVideoContent.Builder()
                        .setVideo(video)
                        .build()
                    shareDialog!!.show(content, ShareDialog.Mode.AUTOMATIC)
                }
                filemanagerstring = selectedImageUri.getPath().toString();
                val selectedImagePath = parsePath(selectedImageUri);
                Log.e("data::-------", selectedImagePath + "" + selectedImageUri)

            }
        }
    }

    fun parsePath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } else null
    }


    fun showDialog( button:String) {
         builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        builder.setTitle("Share")
        val view = layoutInflater.inflate(R.layout.custom_layout, null)
        val linkLayout = view.findViewById<LinearLayout>(R.id.linkLayout)
        val videoLayout = view.findViewById<LinearLayout>(R.id.videoLayout)
        builder.setView(view)
        linkLayout.setOnClickListener {
            val fbConnectHelper = FacebookHelper(this)
            fbConnectHelper.shareOnFBWall(
                "Social Login",
                "Android Facebook and Google+ Login",
                Uri.parse(URL)
            )
        }
        videoLayout.setOnClickListener {
             val intent = Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
              intent.type = "video/*";
            startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO)
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()

    }
}