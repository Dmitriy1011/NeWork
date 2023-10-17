package ru.testapp.nework.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import ru.testapp.nework.BuildConfig

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPS_API_KEY)
    }
}