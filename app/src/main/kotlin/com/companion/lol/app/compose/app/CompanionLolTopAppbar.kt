@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.compose.app

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.companion.lol.app.R
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreview
import com.companion.lol.app.compose.ui.tooling.CompanionAppPreviewWrapperProvider

enum class AppBarNavIcon(val imageVector: ImageVector?) {
  NONE(null),
  UP(Icons.AutoMirrored.Rounded.ArrowBack),
  CLOSE(Icons.Rounded.Close),
}

@Composable
fun CompanionLolTopAppbar(
  modifier: Modifier,
  navIcon: AppBarNavIcon = AppBarNavIcon.NONE,
  @StringRes titleRes: Int? = null,
  colors: TopAppBarColors =
    TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      titleContentColor = MaterialTheme.colorScheme.onPrimary,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
    ),
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
  expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
  scrollBehavior: TopAppBarScrollBehavior? = null,
  actions: @Composable RowScope.() -> Unit = {},
) {
  TopAppBar(
    modifier = modifier,
    title = { titleRes?.let { Text(text = stringResource(id = it)) } },
    colors = colors,
    expandedHeight = expandedHeight,
    navigationIcon = {
      if (navIcon != AppBarNavIcon.NONE) {
        val backPress = LocalOnBackPressedDispatcherOwner.current
        IconButton(onClick = { backPress?.onBackPressedDispatcher?.onBackPressed() }) {
          Icon(imageVector = requireNotNull(navIcon.imageVector), contentDescription = null)
        }
      }
    },
    actions = actions,
    scrollBehavior = scrollBehavior,
    windowInsets = windowInsets,
  )
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun Preview() {
  CompanionLolTopAppbar(
    modifier = Modifier.fillMaxWidth(),
    windowInsets = WindowInsets(),
    expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight + 24.dp,
    titleRes = R.string.app_name,
    navIcon = AppBarNavIcon.UP,
  )
}
