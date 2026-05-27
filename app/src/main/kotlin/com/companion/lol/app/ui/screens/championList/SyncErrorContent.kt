package com.companion.lol.app.ui.screens.championList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.companion.lol.app.R
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreview
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreviewWrapperProvider

@Composable
fun SyncErrorContent(modifier: Modifier = Modifier, onRetry: () -> Unit) {
  Column(
    modifier = modifier.padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      imageVector = Icons.Rounded.ReportProblem,
      contentDescription = null,
      modifier = Modifier.size(80.dp),
      tint = MaterialTheme.colorScheme.error,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = stringResource(R.string.sync_error_title),
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = stringResource(R.string.sync_error_description),
      style = MaterialTheme.typography.bodyLarge,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(onClick = onRetry) {
      Text(
        text = stringResource(R.string.sync_error_retry_button),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
      )
    }
  }
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun SyncErrorContentPreview() {
  SyncErrorContent(onRetry = {})
}
