package com.fastaccess.github.utils.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.fastaccess.github.R
import com.fastaccess.github.base.BaseActivity
import com.fastaccess.github.extensions.getSimpleName
import com.fastaccess.github.ui.modules.routing.RoutingActivity
import timber.log.Timber

/**
 * Created by Kosh on 12.06.18.
 */

fun BaseActivity.replaceIfNotExisting(containerId: Int,
                                      fragment: Fragment,
                                      tag: String? = null,
                                      callback: ((Fragment) -> Unit)? = null) {

    val existingFragment = supportFragmentManager.findFragmentByTag(tag)
    if (existingFragment == null) {
        supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .runOnCommit {
                    callback?.invoke(fragment)
                }
                .commitNow()
    } else {
        callback?.invoke(fragment)
    }
}

fun BaseActivity.replace(containerId: Int,
                         fragment: Fragment,
                         tag: String? = null,
                         callback: ((Fragment) -> Unit)? = null) {
    supportFragmentManager.beginTransaction()
            .replace(containerId, fragment, tag ?: fragment.getSimpleName())
            .runOnCommit {
                callback?.invoke(fragment)
            }
            .commitNow()
}

fun Context.getColorAttr(attr: Int): Int {
    val theme = theme
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
    val color = typedArray.getColor(0, Color.LTGRAY)
    typedArray.recycle()
    return color
}

fun Context.getColorCompat(id: Int): Int = ContextCompat.getColor(this, id)

fun Context.getDrawableCompat(id: Int): Drawable? = ContextCompat.getDrawable(this, id)

fun FragmentActivity.clearDarkStatusBarIcons() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.window?.decorView?.let { decoder ->
            decoder.systemUiVisibility = decoder.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

fun FragmentActivity.setStatusBarColor(colorAttr: Int = R.attr.colorAccent) {
    window?.statusBarColor = getColorAttr(colorAttr)
}

fun Fragment.getDrawable(@DrawableRes drawableRes: Int): Drawable? = ContextCompat.getDrawable(requireContext(), drawableRes)

fun Fragment.route(url: String?) {
    context?.route(url)
}

fun Context.route(url: String?) {
    url?.let {
        Timber.e("routing to: $url")
        val intent = Intent(this, RoutingActivity::class.java)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}

fun BaseActivity.fromDeepLink() = intent?.getBooleanExtra(DeepLink.IS_DEEP_LINK, false) ?: false