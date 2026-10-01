package com.tamilcalendar.data

/**
 * Where the app downloads yearly data updates from. Point this at the folder that
 * contains `manifest.json` (the published `hosting/v1/` folder), ending with '/'.
 *
 * Free options (see hosting/README.md):
 *  - GitHub Pages:   "https://<github-user>.github.io/tamilcalendar-data/v1/"
 *  - jsDelivr CDN:   "https://cdn.jsdelivr.net/gh/<github-user>/tamilcalendar-data@main/v1/"
 *  - Firebase:       "https://<project-id>.web.app/v1/"
 *
 * While it still contains "YOUR_", remote updates are skipped and the app uses the
 * data bundled inside it.
 */
object RemoteConfig {
    const val DATA_BASE_URL = "https://YOUR_GITHUB_USERNAME.github.io/tamilcalendar-data/v1/"

    val enabled: Boolean get() = !DATA_BASE_URL.contains("YOUR_")
}
