package com.sec.instagramclone.ui.common.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.view.ContextThemeWrapper
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RawRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

fun Context?.getActivity(): FragmentActivity? {
    if (this == null) {
        return null
    }

    if (this is Activity) {
        return this as FragmentActivity?
    }
    return try {
        when (this) {
            is ContextThemeWrapper -> {
                var baseContext = this.baseContext
                while (baseContext !is FragmentActivity) {
                    baseContext = baseContext.getActivity()
                }
                return baseContext
            }

            is androidx.appcompat.view.ContextThemeWrapper -> {
                var baseContext = this.baseContext
                while (baseContext !is Activity) {
                    baseContext = baseContext.getActivity()
                }
                return baseContext as FragmentActivity?
            }

            else -> {
                this as FragmentActivity?
            }
        }
    } catch (exception: Exception) {
        null
    }
}

fun <T> Context?.getApp(): T {
    return this?.applicationContext as T
}


fun Context.openFacebook(pageId: String) {

    val facebookPackage = "com.facebook.katana"

    if (isPackageInstalled(facebookPackage)) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/$pageId")
        )
        intent.setPackage(facebookPackage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            openBrowser("http://www.facebook.com/$pageId")
        }
    } else {
        openBrowser("http://www.facebook.com/$pageId")
    }
}

fun Context.openBrowser(url: String?) {
    val browserIntent = Intent(Intent.ACTION_VIEW)
    browserIntent.data = Uri.parse(url)
    if (browserIntent.resolveActivity(packageManager) != null) {
        startActivity(browserIntent)
    }
}

fun Context.openInstagramProfile(username: String) {
    val scheme = "http://instagram.com/"
    openBrowser(scheme + username)
}

fun Context.openLinkedProfile(userId: String, isCompany: Boolean = false) {
    val url = if (isCompany) {
        "https://www.linkedin.com/company/$userId"
    } else {
        "https://www.linkedin.com/in/$userId"
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.setPackage("com.linkedin.android")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        openBrowser(url)
    }
}

fun Context.openTwitter(userId: String) {
    openBrowser("https://twitter.com/${userId.replaceFirst("@", "")}")
}

fun Context.openYoutube(username: String?) {
    val scheme = "http://www.youtube.com/user/"
    openBrowser(scheme + username)
}


fun Context.isPackageInstalled(
    packageName: String?
): Boolean {
    return try {
        packageManager.getPackageInfo(packageName.toString(), 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}


fun Context.readJsonFromRaw(@RawRes rawId: Int): JSONObject {
    val inputStream: InputStream = resources.openRawResource(rawId)
    val writer: Writer = StringWriter()
    val buffer = CharArray(1024)
    inputStream.use { inputStream ->
        val reader: Reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    }

    val jsonString: String = writer.toString()
    return JSONObject(jsonString)
}

fun Context.addOnBackPressedCallback(
    lifecycleOwner: LifecycleOwner,
    handleBackPress: () -> Unit = {}
): OnBackPressedCallback? {
    val activity = getActivity()
    if (activity is FragmentActivity) {
        return activity.addOnBackPressedCallback(lifecycleOwner, handleBackPress)
    }
    return null
}

fun Context.callPhoneNumberInDialer(phoneNumber: String) {
    var phoneNumber = phoneNumber
    phoneNumber = phoneNumber.replace("[^\\d.]".toRegex(), "")
    if (!phoneNumber.startsWith("+") && !phoneNumber.startsWith("0")) {
        phoneNumber = "+$phoneNumber"
    }
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Any.getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun Any.getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun <T> wrapInTryCatch(block: () -> T) = try {
    block()
} catch (e: Exception) {
    e.printStackTrace()
}