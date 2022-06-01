package az.zero.instabugtaskaz.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}


fun <T> LiveData<Event<T>>.observeIfNotHandled(owner: LifecycleOwner, result: (T) -> Unit) {
    this.observe(owner) { event ->
        event.getContentIfNotHandled()?.let {
            result(it)
        }
    }
}