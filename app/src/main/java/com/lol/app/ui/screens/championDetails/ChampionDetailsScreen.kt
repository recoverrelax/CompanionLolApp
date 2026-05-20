package com.lol.app.ui.screens.championDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.companion.lol.app.R
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.lol.app.base.material3.TitleHeader
import com.lol.app.ui.LocalContentPadding
import com.lol.app.util.ChampionColorCache
import com.lol.app.util.DominantColorCoilImage
import com.lol.app.util.EMPTY_STRING
import com.lol.app.util.LocalChampionColorCache
import com.lol.app.util.color
import com.lol.app.util.icon

@Composable
fun ChampionDetailsScreen(championId: ChampionId) {
  val viewModel =
    hiltViewModel<ChampionDetailsViewModel, ChampionDetailsViewModel.Factory>(
      creationCallback = { factory -> factory.create(championId) }
    )
  val state by viewModel.state.collectAsState()

  ChampionDetailsScreen(state = state, onFavoritesClicked = viewModel::onFavoritesClicked)
}

@Composable
fun ChampionDetailsScreen(state: ChampionDetailsState, onFavoritesClicked: () -> Unit) {
  val championId = state.championId
  val championColorCache = LocalChampionColorCache.current
  val dominantColor = championColorCache.getColor(championId)

  Column(
    modifier =
      Modifier.fillMaxWidth()
        .padding(bottom = LocalContentPadding.current.calculateBottomPadding().plus(32.dp))
  ) {
    ImageHeader(
      championId = championId,
      skins = state.details?.skins ?: emptyList(),
      championColorCache = championColorCache,
      onFavoritesClicked = onFavoritesClicked,
      isFavourite = state.champion?.isFavorite ?: false,
      dominantColor = dominantColor,
      championName = state.champion?.name ?: EMPTY_STRING,
      championTitle = state.champion?.title ?: EMPTY_STRING,
    )

    Row {
      ChampionPartyType(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp).weight(1f),
        header = stringResource(R.string.champion_details_party_type),
        label = state.champion?.partyType?.label?.uppercase() ?: EMPTY_STRING,
        color = state.champion?.partyType?.color ?: dominantColor,
      )

      ChampionClass(
        modifier = Modifier.padding(end = 16.dp, top = 16.dp),
        headerTitle = stringResource(R.string.champion_details_class),
        tags = state.details?.tags ?: emptyList(),
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    TitleHeader(
      modifier = Modifier.padding(bottom = 8.dp, top = 16.dp, start = 16.dp),
      label = stringResource(id = R.string.champion_details_lore),
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      text = state.details?.lore ?: EMPTY_STRING,
      style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight),
      textAlign = TextAlign.Justify,
    )
  }
}

@Composable
private fun ImageHeader(
  championId: ChampionId,
  skins: List<ChampionSkin>,
  championColorCache: ChampionColorCache,
  onFavoritesClicked: () -> Unit,
  isFavourite: Boolean,
  dominantColor: Color,
  championName: String,
  championTitle: String,
) {
  val championSkins = rememberChampionSkinImageProvider(championId = championId, skins = skins)
  val topActionIconBg = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
  val iconTint = MaterialTheme.colorScheme.onSurface

  Box(modifier = Modifier.fillMaxWidth().aspectRatio(1215f / 717f)) {
    DominantColorCoilImage(
      modifier = Modifier.fillMaxSize(),
      championId = championId,
      image = championSkins.image,
      championColorCache = championColorCache,
      skipUpdateColorCache = true,
    )

    Icon(
      modifier =
        Modifier.padding(20.dp)
          .size(32.dp)
          .background(color = topActionIconBg, shape = MaterialTheme.shapes.small)
          .clickable(onClick = onFavoritesClicked)
          .padding(4.dp)
          .align(Alignment.TopStart),
      imageVector = if (isFavourite) Icons.Rounded.Star else Icons.Outlined.StarBorder,
      contentDescription = null,
      tint = if (!isFavourite) iconTint else dominantColor,
    )

    Icon(
      modifier =
        Modifier.padding(16.dp)
          .size(32.dp)
          .background(color = topActionIconBg, shape = MaterialTheme.shapes.small)
          .clickable(onClick = championSkins::toggleSkin)
          .padding(4.dp)
          .align(Alignment.TopEnd),
      imageVector = Icons.Rounded.Refresh,
      contentDescription = null,
      tint = iconTint,
    )

    val textOverlapGradient =
      remember(dominantColor) {
        Brush.verticalGradient(
          colors = listOf(Color.Transparent, dominantColor.copy(alpha = 0.7f)),
          tileMode = TileMode.Mirror,
        )
      }

    Column(
      modifier =
        Modifier.fillMaxWidth()
          .align(Alignment.BottomCenter)
          .drawBehind { drawRect(brush = textOverlapGradient) }
          .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.height(IntrinsicSize.Max).offset(x = (-4).dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = championName, style = MaterialTheme.typography.headlineMedium)
      }
      Text(
        text = championTitle,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Light),
      )
    }
  }
}

@Composable
fun ChampionPartyType(modifier: Modifier, header: String, label: String, color: Color) {
  Box(modifier = modifier) {
    Row(
      modifier = Modifier.height(IntrinsicSize.Max),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      TitleHeader(modifier = Modifier, label = header)
      Spacer(modifier = Modifier.width(8.dp))
      Box(
        modifier =
          Modifier.padding(vertical = 2.dp)
            .fillMaxHeight()
            .background(color, MaterialTheme.shapes.small)
            .padding(2.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          modifier = Modifier.widthIn(min = 40.dp),
          text = label,
          style = MaterialTheme.typography.labelSmall,
          textAlign = TextAlign.Center,
        )
      }
    }
  }
}

@Composable
fun ChampionClass(modifier: Modifier, headerTitle: String, tags: List<ChampionTag>) {
  Row(modifier) {
    TitleHeader(modifier = Modifier, label = headerTitle)

    Column(
      modifier = Modifier.widthIn(min = 32.dp).padding(start = 8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      for (tag in tags) {
        key(tag.dbId) { Tag(icon = tag.icon, label = tag.label) }
      }
    }
  }
}

@Composable
private fun Tag(
  modifier: Modifier = Modifier,
  icon: Int,
  label: String,
  tagBackground: Color = MaterialTheme.colorScheme.surfaceVariant,
  tagColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
  Row(
    modifier =
      modifier
        .background(tagBackground, MaterialTheme.shapes.medium)
        .padding(horizontal = 10.dp, vertical = 5.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      modifier = Modifier.alignByBaseline().width(16.dp).aspectRatio(1f),
      painter = painterResource(id = icon),
      contentDescription = null,
      tint = Color.Unspecified,
    )
    Text(
      modifier = Modifier.padding(start = 4.dp),
      text = label,
      color = tagColor,
      textAlign = TextAlign.Start,
      style = MaterialTheme.typography.labelSmall,
    )
  }
}
