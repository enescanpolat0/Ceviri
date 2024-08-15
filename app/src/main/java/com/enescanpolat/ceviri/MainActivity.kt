package com.enescanpolat.ceviri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enescanpolat.ceviri.util.Screen
import com.enescanpolat.ceviri.view.TranslateScreen
import com.enescanpolat.ceviri.view.uiTheme.CeviriTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CeviriTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TranslateScreen.route){
                    composable(route = Screen.TranslateScreen.route){
                        TranslateScreen(navController)
                    }
                }

            }
        }
    }
}

