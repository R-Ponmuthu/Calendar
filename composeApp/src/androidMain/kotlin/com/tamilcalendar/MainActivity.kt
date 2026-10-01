package com.tamilcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tamilcalendar.ads.AndroidAds
import com.tamilcalendar.data.LocalStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        LocalStore.init(applicationContext)
        AndroidAds.init(this)
        setContent { App() }
    }
}
