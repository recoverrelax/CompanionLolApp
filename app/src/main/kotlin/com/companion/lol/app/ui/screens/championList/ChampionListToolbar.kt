package com.companion.lol.app.ui.screens.championList

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.companion.lol.app.R
import com.companion.lol.app.compose.app.AppBarNavIcon
import com.companion.lol.app.compose.app.CompanionLolTopAppbar
import com.companion.lol.app.compose.app.companionAppGradientInverted
import com.companion.lol.storage.impl.model.other.SortOrder

@ExperimentalMaterial3Api
@Composable
fun ChampionListToolbar(
  modifier: Modifier,
  scrollBehavior: TopAppBarScrollBehavior? = null,
  sortOrder: SortOrder,
  onGridSizeItemMenuClicked: () -> Unit,
  onSortMenuItemClicked: () -> Unit,
  onClearFavoritesMenuItemClicked: () -> Unit,
) {

  val gradient = companionAppGradientInverted

  val alpha =
    remember(scrollBehavior) {
      derivedStateOf {
        if (scrollBehavior == null) {
          1f
        } else {
          val fraction = 1f - scrollBehavior.state.collapsedFraction
          LinearOutSlowInEasing.transform(fraction)
        }
      }
    }

  CompanionLolTopAppbar(
    modifier = modifier.drawBehind { drawRect(brush = gradient, alpha = alpha.value) },
    colors =
      TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
      ),
    scrollBehavior = scrollBehavior,
    navIcon = AppBarNavIcon.CLOSE,
    titleRes = R.string.champion_list,
    actions = {
      var expanded by remember { mutableStateOf(false) }

      IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
      }

      DropdownMenu(
        modifier = Modifier.padding(horizontal = 8.dp),
        expanded = expanded,
        onDismissRequest = { expanded = false },
        properties = PopupProperties(focusable = true),
        containerColor = MaterialTheme.colorScheme.surface,
      ) {
        DropDownMenuItem(
          title = R.string.champion_list_grid_count,
          icon = Icons.AutoMirrored.Filled.List,
          onClick = {
            onGridSizeItemMenuClicked()
            expanded = false
          },
        )

        DropDownMenuItem(
          title =
            when (sortOrder) {
              SortOrder.ASC -> R.string.champion_list_sort_favorites
              SortOrder.FAVORITES -> R.string.champion_list_sort_alphabetically
            },
          icon =
            when (sortOrder) {
              SortOrder.ASC -> Icons.Default.Star
              SortOrder.FAVORITES -> Icons.Default.KeyboardArrowDown
            },
          onClick = {
            onSortMenuItemClicked()
            expanded = false
          },
        )

        DropDownMenuItem(
          title = R.string.champion_list_clear_favorites,
          icon = Icons.Rounded.Delete,
          onClick = {
            onClearFavoritesMenuItemClicked()
            expanded = false
          },
        )
      }
    },
  )
}

@Composable
private fun DropDownMenuItem(@StringRes title: Int, icon: ImageVector, onClick: () -> Unit) {
  DropdownMenuItem(
    modifier = Modifier.fillMaxWidth(),
    onClick = onClick,
    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
    text = {
      Text(
        modifier = Modifier.padding(horizontal = 10.dp),
        text = stringResource(title),
        color = MaterialTheme.colorScheme.onSurface,
      )
    },
    leadingIcon = {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurface,
      )
    },
    colors =
      MenuItemColors(
        textColor = MaterialTheme.colorScheme.primary,
        leadingIconColor = MaterialTheme.colorScheme.primary,
        trailingIconColor = MaterialTheme.colorScheme.primary,
        disabledTextColor = MaterialTheme.colorScheme.primary,
        disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
        disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
      ),
  )
}
