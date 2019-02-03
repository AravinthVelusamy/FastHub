package com.fastaccess.markdown.spans.drawable

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.fastaccess.markdown.R

internal class GlideDrawableTarget<T : Drawable>(
    private val urlDrawable: UrlDrawable,
    private val container: TextView?,
    private val width: Int
) : SimpleTarget<T>() {

    override fun onResourceReady(resource: T, transition: Transition<in T>?) {
        val textView = container
        textView?.post {
            val width: Float
            val height: Float
            if (resource.intrinsicWidth >= this.width) {
                val downScale = resource.intrinsicWidth.toFloat() / this.width
                width = (resource.intrinsicWidth.toDouble() / downScale.toDouble() / 1.3).toFloat()
                height = (resource.intrinsicHeight.toDouble() / downScale.toDouble() / 1.3).toFloat()
            } else {
                val multiplier = this.width.toFloat() / resource.intrinsicWidth
                width = resource.intrinsicWidth.toFloat() * multiplier
                height = resource.intrinsicHeight.toFloat() * multiplier
            }
            val rect = Rect(0, 0, Math.round(width), Math.round(height))
            resource.bounds = rect
            urlDrawable.bounds = rect
            urlDrawable.setDrawable(resource)
            urlDrawable.callback = textView.getTag(R.id.drawable_callback) as DrawableGetter
            if (resource is GifDrawable) {
                resource.setLoopCount(GifDrawable.LOOP_FOREVER)
                resource.start()
            }
            textView.text = textView.text
            textView.invalidate()
        }
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        applyTempDrawable(placeholder)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        applyTempDrawable(errorDrawable)
    }

    private fun applyTempDrawable(placeholder: Drawable?) {
        placeholder?.let { drawable ->
            val rect = Rect(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.bounds = rect
            urlDrawable.bounds = rect
            urlDrawable.setDrawable(drawable)
            container?.let {
                it.text = it.text
                it.invalidate()
            }
        }
    }
}