package com.marchuck.a3xisrael.base

import android.os.Handler
import android.os.Looper
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.widget.Toast

open class BaseFragment<out T : FragmentActivity> : Fragment() {

    enum class Duration {
        LONG, SHORT;

        fun toastValue(): Int {
            return if (this == SHORT) {
                Toast.LENGTH_SHORT
            } else Toast.LENGTH_LONG
        }

        fun snackBarValue(): Int {
            return if (this == SHORT) {
                Snackbar.LENGTH_SHORT
            } else Snackbar.LENGTH_LONG
        }
    }

    val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    @Suppress("UNCHECKED_CAST")
    fun getParentActivity(): T = activity as T

    fun showMessage(message: String, duration: Duration = Duration.SHORT) {
        if (isOnMainThread()) {
            if (view is CoordinatorLayout) {
                Snackbar.make(view!!, message, duration.snackBarValue()).show()
            } else {
                Toast.makeText(activity, message, duration.toastValue()).show()
            }
        } else {
            handler.post({ showMessage(message) })
        }
    }

    private fun isOnMainThread() = Looper.getMainLooper() == Looper.myLooper()
}