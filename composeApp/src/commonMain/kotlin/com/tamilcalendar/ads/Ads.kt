package com.tamilcalendar.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tamilcalendar.currentEpochMillis

/** Anchored banner shown at the bottom of every screen (renders nothing where ads are unavailable). */
@Composable
expect fun BannerAd(modifier: Modifier = Modifier)

/** Shows a preloaded interstitial if one is ready; otherwise just starts loading one. */
expect fun showInterstitialAd()

/**
 * Interstitial frequency cap: at most one every [EVERY_N] menu changes and never more
 * often than [MIN_GAP_MS]. Nothing is shown on app start.
 */
object AdPolicy {
    private const val EVERY_N = 3
    private const val MIN_GAP_MS = 90_000L
    private var navigations = 0
    private var lastShown = 0L

    fun onScreenChanged() {
        navigations++
        val now = currentEpochMillis()
        if (navigations % EVERY_N == 0 && now - lastShown >= MIN_GAP_MS) {
            lastShown = now
            showInterstitialAd()
        }
    }
}
