package au.com.appetiser.isearch

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber

class ISearchApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        ProcessLifecycleOwner.get()
                             .lifecycle
                             .addObserver(AppLifecycleObserver(applicationContext))
    }

}