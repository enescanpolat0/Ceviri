package com.enescanpolat.ceviri.viewmodel

sealed class TranslateEvent {
    data class translate(val translateString: String):TranslateEvent()
}