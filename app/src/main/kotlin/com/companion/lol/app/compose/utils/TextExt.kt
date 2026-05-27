package com.companion.lol.app.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun rememberSaveableTextFieldState(initialValue: String): MutableState<TextFieldValue> {
  return rememberSaveable(stateSaver = TextFieldValue.Saver) {
    mutableStateOf(TextFieldValue(initialValue))
  }
}
