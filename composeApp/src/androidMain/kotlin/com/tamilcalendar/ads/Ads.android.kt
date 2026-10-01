package com.tamilcalendar.ads

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.tamilcalendar.BuildConfig
import java.lang.ref.WeakReference

/** Google AdMob. Debug builds use Google's test ad units (see composeApp/build.gradle.kts). */
object AndroidAds {
    private var activityRef: WeakReference<Activity>? = null
    private var interstitial: InterstitialAd? = null
    private var loading = false
    private var initialized = false

    fun init(activity: Activity) {
        activityRef = WeakReference(activity)
        if (initialized) return
        initialized = true
        MobileAds.initialize(activity.applicationContext) { preloadInterstitial() }
    }

    fun preloadInterstitial() {
        val activity = activityRef?.get() ?: return
        if (interstitial != null || loading) return
        loading = true
        InterstitialAd.load(
            activity,
            BuildConfig.ADMOB_INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitial = ad
                    loading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitial = null
                    loading = false
                }
            },
        )
    }

    fun show() {
        val activity = activityRef?.get() ?: return
        val ad = interstitial ?: run { preloadInterstitial(); return }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitial = null
                preloadInterstitial()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitial = null
                preloadInterstitial()
            }
        }
        ad.show(activity)
    }
}

actual fun showInterstitialAd() = AndroidAds.show()

@Composable
actual fun BannerAd(modifier: Modifier) {
    val widthDp = LocalConfiguration.current.screenWidthDp
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            AdView(ctx).apply {
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, widthDp))
                adUnitId = BuildConfig.ADMOB_BANNER_ID
                loadAd(AdRequest.Builder().build())
            }
        },
        onRelease = { it.destroy() },
    )
}
