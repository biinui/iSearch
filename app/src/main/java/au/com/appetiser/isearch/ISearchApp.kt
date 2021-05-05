package au.com.appetiser.isearch

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber

class ISearchApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        addLifecycleObserver()

    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun addLifecycleObserver() {
        ProcessLifecycleOwner.get()
                             .lifecycle
                             .addObserver(AppLifecycleObserver(applicationContext))
    }

}