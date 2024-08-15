package com.enescanpolat.ceviri.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import com.enescanpolat.ceviri.viewmodel.TranslateEvent
import com.enescanpolat.ceviri.viewmodel.TranslateViewModel

@Composable
fun TranslateScreen(navController: NavController,viewModel: TranslateViewModel= hiltViewModel()) {

    val translation by viewModel.translateWord.observeAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Green)){

        Column {
            TranslateBar(modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
                hint = "ENTER TEXT",
                onTranslate = {
                    viewModel.onEvent(TranslateEvent.translate(it))
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp).width(16.dp))

            Text(text = translation?.data?:"NO DATA",
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
                )


        }




    }




}

@Composable
fun TranslateBar(
    modifier: Modifier,
    hint: String = "",
    onTranslate: (String) -> Unit = {}
) {

    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier){

        TextField(value = text,
            keyboardActions = KeyboardActions(onDone = {onTranslate(text)}),
            onValueChange = {text = it},
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }

            )

        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }


    }



}