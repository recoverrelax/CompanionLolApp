package com.companion.lol.app.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.companion.lol.app.R
import com.companion.lol.app.compose.app.CompanionAppSurface
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreview
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreviewWrapperProvider
import com.companion.lol.app.compose.ui.tooling.LandscapePreview
import com.companion.lol.app.compose.utils.isLandscape
import com.companion.lol.app.compose.utils.rememberSaveableTextFieldState
import com.companion.lol.app.ui.LocalContentPadding

@Composable
fun LoginScreen() {
  val viewModel: LoginViewModel = hiltViewModel()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LoginScreen(
    state = state,
    onEmailChanged = viewModel::onEmailChanged,
    onLoginClicked = viewModel::onLoginClicked,
  )
}

@Composable
fun LoginScreen(state: LoginState, onEmailChanged: (String) -> Unit, onLoginClicked: () -> Unit) {
  val isLandscape = isLandscape()
  val widthModifier = Modifier.fillMaxWidth(if (isLandscape) 0.5f else 1f)

  CompanionAppSurface(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.padding(LocalContentPadding.current)) {
      Column(
        modifier = widthModifier.weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "Welcome",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onBackground,
          textAlign = TextAlign.Center,
        )

        Text(
          text = "Sign in to your account",
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
          textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        rememberSaveableTextFieldState(state.email)

        OutlinedTextField(
          value = state.email,
          onValueChange = onEmailChanged,
          label = {
            Text("Email Address", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
          },
          modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth(),
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors =
            OutlinedTextFieldDefaults.colors(
              unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
              focusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
              unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            ),
          keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        )
      }

      Button(
        onClick = onLoginClicked,
        enabled = state.isEmailValid,
        modifier = widthModifier.imePadding().padding(32.dp).fillMaxWidth(),
      ) {
        Text(text = stringResource(R.string.login_continue))
      }
    }
  }
}

@Composable
@CompanionAppPreview
@LandscapePreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun LoginPreview() {
  LoginScreen(state = LoginState(email = ""), onEmailChanged = {}, onLoginClicked = {})
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun LoginPreview2() {
  LoginScreen(state = LoginState(), onEmailChanged = {}, onLoginClicked = {})
}
