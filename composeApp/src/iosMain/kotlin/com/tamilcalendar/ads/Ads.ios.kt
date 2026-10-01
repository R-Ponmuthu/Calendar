package com.tamilcalendar.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

/**
 * Implemented in Swift with the Google Mobile Ads SDK (iosApp/iosApp/AdMobBridge.swift)
 * and registered at launch: `IosAds.shared.bridge = AdMobBridge()`.
 * Until a bridge is registered, no ads are shown on iOS.
 */
interface IosAdsBridge {
    fun makeBannerView(): UIView
    fun showInterstitial()
}

object IosAds {
    var bridge: IosAdsBridge? = null
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BannerAd(modifier: Modifier) {
    val bridge = IosAds.bridge ?: return
    UIKitView(
        factory = { bridge.makeBannerView() },
        modifier = modifier.fillMaxWidth().height(50.dp),
    )
}

actual fun showInterstitialAd() {
    IosAds.bridge?.showInterstitial()
}
