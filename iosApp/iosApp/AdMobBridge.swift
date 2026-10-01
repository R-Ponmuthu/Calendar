// AdMob for iOS – OPTIONAL. This file is NOT part of the Xcode target yet.
//
// To enable ads on iOS:
//  1. Xcode ▸ File ▸ Add Package Dependencies… ▸
//     https://github.com/googleads/swift-package-manager-google-mobile-ads  (GoogleMobileAds, v12+)
//  2. Drag this file into the iosApp group (tick the iosApp target).
//  3. In iOSApp.swift add:   init() { IosAds.shared.bridge = AdMobBridge() }
//  4. Replace the test ids below and GADApplicationIdentifier in Info.plist with your own.
import GoogleMobileAds
import ComposeApp
import UIKit

final class AdMobBridge: NSObject, IosAdsBridge, FullScreenContentDelegate {
    // Google test ad units – replace before release.
    private let bannerUnitId = "ca-app-pub-3940256099942544/2934735716"
    private let interstitialUnitId = "ca-app-pub-3940256099942544/4411468910"

    private var interstitial: InterstitialAd?
    private var loading = false

    override init() {
        super.init()
        MobileAds.shared.start { [weak self] _ in self?.loadInterstitial() }
    }

    func makeBannerView() -> UIView {
        let banner = BannerView(adSize: AdSizeBanner)
        banner.adUnitID = bannerUnitId
        banner.rootViewController = AdMobBridge.topViewController()
        banner.load(Request())
        return banner
    }

    func showInterstitial() {
        guard let ad = interstitial, let vc = AdMobBridge.topViewController() else {
            loadInterstitial()
            return
        }
        ad.present(from: vc)
    }

    private func loadInterstitial() {
        guard interstitial == nil, !loading else { return }
        loading = true
        InterstitialAd.load(with: interstitialUnitId, request: Request()) { [weak self] ad, _ in
            guard let self else { return }
            self.loading = false
            self.interstitial = ad
            ad?.fullScreenContentDelegate = self
        }
    }

    func adDidDismissFullScreenContent(_ ad: FullScreenPresentingAd) {
        interstitial = nil
        loadInterstitial()
    }

    func ad(_ ad: FullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        interstitial = nil
        loadInterstitial()
    }

    private static func topViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes.compactMap { $0 as? UIWindowScene }.first
        var top = scene?.windows.first { $0.isKeyWindow }?.rootViewController
        while let presented = top?.presentedViewController { top = presented }
        return top
    }
}
