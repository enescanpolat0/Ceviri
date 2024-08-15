package com.enescanpolat.ceviri.util

sealed class Screen(val route:String) {
    object TranslateScreen:Screen("translate_screen")
}