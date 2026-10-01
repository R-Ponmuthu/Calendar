package com.tamilcalendar.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** No ads on desktop. */
@Composable
actual fun BannerAd(modifier: Modifier) {}

actual fun showInterstitialAd() {}
