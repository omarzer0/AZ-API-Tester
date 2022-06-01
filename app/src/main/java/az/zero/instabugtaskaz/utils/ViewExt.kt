package az.zero.instabugtaskaz.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.toggle() {
    if (this.isVisible) this.gone()
    else this.show()
}