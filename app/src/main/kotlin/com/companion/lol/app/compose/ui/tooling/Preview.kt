package com.companion.lol.app.compose.ui.tooling

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.companion.lol.app.compose.ui.theme.CompanionAppTheme

@Preview
// @Preview(uiMode = UI_MODE_NIGHT_YES)
annotation class CompanionAppPreview

class CompanionAppPreviewWrapperProvider : PreviewWrapperProvider {
  @Composable
  override fun Wrap(content: @Composable () -> Unit) {
    // Wrap the content in a Material3 Scaffold to provide a standard app structure
    CompanionAppTheme { content() }
  }
}
