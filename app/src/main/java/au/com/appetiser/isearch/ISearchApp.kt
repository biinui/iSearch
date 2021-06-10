package au.com.appetiser.isearch

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import au.com.appetiser.isearch.di.AppComponent
import au.com.appetiser.isearch.di.AppModule
import au.com.appetiser.isearch.di.DaggerAppComponent
import timber.log.Timber

class ISearchApp: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                          .appModule(AppModule(this))
                          .build()
    }

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