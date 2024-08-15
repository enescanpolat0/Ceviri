package com.enescanpolat.ceviri.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enescanpolat.ceviri.util.Resource
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor() :ViewModel() {

    private val _translateWord = MutableLiveData<Resource<String?>>()
    val translateWord : LiveData<Resource<String?>> = _translateWord


    private var _identifiedLanguage = MutableLiveData<String?>()
    val identifiedLanguage: LiveData<String?>  = _identifiedLanguage




    private var job : Job?=null





    fun translation(translateText:String){

        job?.cancel()


        job = viewModelScope.launch {
            languageSoruce(translateText)
            _translateWord.postValue(Resource.Loading())
            println("loading")






            val options = TranslatorOptions.Builder()
                .setSourceLanguage(identifiedLanguage.value.toString())
                .setTargetLanguage(TranslateLanguage.TURKISH)
                .build()

            val translator = Translation.getClient(options)
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translator.translate(translateText)
                        .addOnSuccessListener {textTranslated->
                            _translateWord.postValue(Resource.Success(data = textTranslated.toString()))
                            println(textTranslated.toString())
                            println("success")

                        }
                        .addOnFailureListener {
                            _translateWord.postValue(Resource.Error("Cannot be translate!"))
                        }
                }
                .addOnFailureListener {
                    _translateWord.postValue(Resource.Error("NO INTERNET"))
                }



        }







    }




    private suspend fun languageSoruce(text:String) {


        viewModelScope.launch {
            val languageIdentifier = LanguageIdentification.getClient(
                LanguageIdentificationOptions.Builder()
                    .setConfidenceThreshold(0.8f)
                    .build()
            )



            return@launch withContext(Dispatchers.IO) {
                languageIdentifier.identifyPossibleLanguages(text)
                    .addOnSuccessListener {lonaguagecodes->

                        for (languagecode in lonaguagecodes){
                            _identifiedLanguage.postValue(languagecode.languageTag)
                            println(languagecode.languageTag)
                        }

                    }
                    .addOnFailureListener{
                        println("error")
                    }


            }


        }


    }



    fun onEvent(event: TranslateEvent){
        when(event){
            is TranslateEvent.translate->{
                translation(event.translateString)
            }
        }
    }





}

