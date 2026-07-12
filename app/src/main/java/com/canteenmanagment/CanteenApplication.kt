package com.canteenmanagment

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.canteenmanagment.helper.applySystemBarInsets

/**
 * Applies system bar insets to every activity's content view automatically, so no
 * individual screen needs to opt in. See [applySystemBarInsets] for why this is needed
 * on targetSdk 36 (Android 16 disables the windowOptOutEdgeToEdgeEnforcement escape hatch).
 */
class CanteenApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.findViewById<android.view.View>(android.R.id.content).applySystemBarInsets()
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
