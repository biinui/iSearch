package au.com.appetiser.isearch

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber
import java.util.*

class AppLifecycleObserver(private val applicationContext: Context) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        val now = Date().time
        val sharedPref = applicationContext.getSharedPreferences(
            applicationContext.getString(R.string.preference_file),
            Context.MODE_PRIVATE
        ) ?: return
        with(sharedPref.edit()) {
            putLong(applicationContext.getString(R.string.last_user_visit_timestamp), now)
            apply()
        }
    }

}