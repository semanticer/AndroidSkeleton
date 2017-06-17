package cz.leaderboard.app


import com.bluelinelabs.conductor.Controller
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

import javax.inject.Inject

import cz.leaderboard.app.di.DaggerApplicationComponent
import cz.leaderboard.app.di.conductorlib.HasControllerInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Created by tomas.valenta on 5/11/2017.
 */

class App : dagger.android.support.DaggerApplication(), cz.leaderboard.app.di.conductorlib.HasControllerInjector {

    @javax.inject.Inject
    lateinit var dispatchingControllerInjector: dagger.android.DispatchingAndroidInjector<Controller>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        refWatcher = LeakCanary.install(this)
        Stetho.initializeWithDefaults(this)
    }

    override fun controllerInjector(): DispatchingAndroidInjector<Controller> {
        return dispatchingControllerInjector
    }

    companion object {
        lateinit var refWatcher: RefWatcher
    }
}
