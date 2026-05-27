package com.companion.lol.app.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.companion.lol.app.R
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreview
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreviewWrapperProvider
import com.companion.lol.app.ui.LocalContentPadding

private val rowShape = RoundedCornerShape(12.dp)

@Composable
fun SettingsScreen() {
  val viewModel: SettingsViewModel = hiltViewModel()
  val state by viewModel.state.collectAsStateWithLifecycle()

  SettingsScreen(
    state = state,
    onAutoSyncChanged = viewModel::onAutoSyncChanged,
    onLogoutClicked = viewModel::onLogoutClicked,
  )
}

@Composable
fun SettingsScreen(
  state: SettingsState?,
  onAutoSyncChanged: (Boolean) -> Unit,
  onLogoutClicked: () -> Unit,
) {
  val contentPadding = LocalContentPadding.current

  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Box(
      modifier =
        Modifier.padding(top = contentPadding.calculateTopPadding())
          .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
      Column {
        Text(
          text = stringResource(R.string.settings_title),
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          modifier = Modifier.padding(bottom = 32.dp),
        )

        AnimatedVisibility(
          visible = state != null,
          enter = fadeIn() + slideInVertically { it / 8 },
          exit = ExitTransition.None,
        ) {
          if (state != null) {
            Column {
              EmailDisplay(email = state.emailAddress)

              Spacer(modifier = Modifier.height(16.dp))

              AutoSyncToggle(
                autoSyncChecked = state.autoSync,
                onAutoSyncChanged = onAutoSyncChanged,
              )
            }
          }
        }
      }

      Button(
        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
        onClick = onLogoutClicked,
      ) {
        Text(text = stringResource(R.string.settings_logout))
      }
    }
  }
}

@Composable
private fun EmailDisplay(email: String) {
  Surface(shape = rowShape, modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Text(
        text = stringResource(R.string.settings_email),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
      )
      Text(
        text = email,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}

@Composable
private fun AutoSyncToggle(autoSyncChecked: Boolean, onAutoSyncChanged: (Boolean) -> Unit) {
  Surface(shape = rowShape, modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier =
        Modifier.fillMaxWidth().clickable { onAutoSyncChanged(!autoSyncChecked) }.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = stringResource(R.string.settings_auto_sync),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
      )
      // we do the switching on the parent. Full row click better UI
      Switch(checked = autoSyncChecked, onCheckedChange = null)
    }
  }
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun SettingsPreview() {
  SettingsScreen(
    state = SettingsState(emailAddress = "email", autoSync = true),
    onLogoutClicked = {},
    onAutoSyncChanged = {},
  )
}
