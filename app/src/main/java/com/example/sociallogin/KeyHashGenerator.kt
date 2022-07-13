package com.example.sociallogin

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

public class KeyHashGenerator {
    companion object {
        fun generateKey(activity: Activity?): String? {
            var packageInfo: PackageInfo
            var key: String? = null
            try {
                //getting application package name, as defined in manifest
                val packageName = activity!!.applicationContext.packageName
                //Retrieving package info

                //Retrieving package info
                packageInfo =
                    activity.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNATURES
                    )
                Log.i("Package Name= ", activity.applicationContext.packageName)
                for (signature in packageInfo.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    key = String(Base64.encode(md.digest(), 0))
                    Log.i("Key Hash= ", key)
                }

            } catch (e1: PackageManager.NameNotFoundException) {
                Log.e("Name not found", e1.toString());
            } catch (e: NoSuchAlgorithmException) {
                Log.e("No such an algorithm", e.toString());
            } catch (e: Exception) {
                Log.e("Exception", e.toString());
            }

            return key
        }

    }

}