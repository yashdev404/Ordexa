package com.example.ordexa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ordexa.ui.navigation.OrdexaNavGraph
import com.example.ordexa.ui.theme.OrdexaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrdexaTheme {
                OrdexaNavGraph()
            }
        }
    }
}
