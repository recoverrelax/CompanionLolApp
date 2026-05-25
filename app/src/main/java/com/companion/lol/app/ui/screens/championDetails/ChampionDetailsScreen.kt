package com.companion.lol.app.ui.screens.championDetails

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.companion.lol.app.R
import com.companion.lol.app.compose.app.TitleHeader
import com.companion.lol.app.io.UiError
import com.companion.lol.app.ui.LocalContentPadding
import com.companion.lol.app.ui.LocalSnackBarManager
import com.companion.lol.app.ui.SnackBarManager
import com.companion.lol.app.util.ChampionColorCache
import com.companion.lol.app.util.DominantColorCoilImage
import com.companion.lol.app.util.EMPTY_STRING
import com.companion.lol.app.util.LocalChampionColorCache
import com.companion.lol.app.util.UiMessageEventFlow
import com.companion.lol.app.util.color
import com.companion.lol.app.util.icon
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.companion.lol.storage.impl.model.other.PartyType

@Composable
fun ChampionDetailsScreen(championId: ChampionId, goBack: () -> Unit) {
  val viewModel =
    hiltViewModel<ChampionDetailsViewModel, ChampionDetailsViewModel.Factory>(
      creationCallback = { factory -> factory.create(championId) }
    )
  val state by viewModel.state.collectAsStateWithLifecycle()
  val snackBarManager = LocalSnackBarManager.current

  ChampionDetailsScreen(
    state = state,
    snackBarManager = snackBarManager,
    uiErrors = viewModel.uiErrors,
    goBack = goBack,
    onFavoritesClicked = viewModel::onFavoritesClicked,
  )
}

@Composable
fun ChampionDetailsScreen(
  state: ChampionDetailsState,
  snackBarManager: SnackBarManager,
  uiErrors: UiMessageEventFlow<UiError>,
  goBack: () -> Unit,
  onFavoritesClicked: () -> Unit,
) {
  val championId = state.championId
  val championColorCache = LocalChampionColorCache.current

  val championSkinsProvider =
    rememberChampionSkinImageProvider(championId = championId, skins = state.details?.skins)

  LaunchedEffect(uiErrors) {
    uiErrors.collect {
      snackBarManager.addError(it)
      goBack()
    }
  }

  Column(
    modifier =
      Modifier.fillMaxWidth()
        .padding(
          bottom =
            LocalContentPadding.current
              .calculateBottomPadding()
              .plus(if (state.details == null) 0.dp else 32.dp)
        )
  ) {
    ImageHeader(
      championId = championId,
      championSkins = championSkinsProvider,
      championColorCache = championColorCache,
      onFavoritesClicked = onFavoritesClicked,
      isFavourite = state.champion?.isFavorite ?: false,
      championName = state.champion?.name ?: EMPTY_STRING,
      championTitle = state.champion?.title ?: EMPTY_STRING,
      loaded = state.champion != null && state.details != null,
    )

    if (state.champion == null || state.details == null) {
      LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        color = championColorCache.getColor(championId),
        trackColor = MaterialTheme.colorScheme.onSurface,
        gapSize = 0.dp,
      )
    } else {
      Row {
        ChampionPartyType(
          modifier = Modifier.padding(start = 16.dp, top = 16.dp).weight(1f),
          header = stringResource(R.string.champion_details_party_type),
          partyType = state.champion.partyType,
        )

        ChampionClass(
          modifier = Modifier.padding(end = 16.dp, top = 16.dp),
          headerTitle = stringResource(R.string.champion_details_class),
          tags = state.details.tags,
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
        text = state.details.lore,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight),
        textAlign = TextAlign.Justify,
      )
    }
  }
}

@Composable
private fun ImageHeader(
  championId: ChampionId,
  championSkins: ChampionSkinImagesProvider,
  championColorCache: ChampionColorCache,
  onFavoritesClicked: () -> Unit,
  isFavourite: Boolean,
  championName: String,
  championTitle: String,
  loaded: Boolean,
) {
  val topActionIconBg = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
  val iconTint = MaterialTheme.colorScheme.onSurface
  val dominantColor = championColorCache.getColor(championId)

  Box(modifier = Modifier.fillMaxWidth().aspectRatio(1215f / 717f)) {
    DominantColorCoilImage(
      modifier = Modifier.fillMaxSize(),
      championId = championId,
      image = championSkins.imageInfo,
      championColorCache = championColorCache,
      skipUpdateColorCache = true,
    )

    Icon(
      modifier =
        Modifier.padding(20.dp)
          .size(32.dp)
          .background(color = topActionIconBg, shape = MaterialTheme.shapes.small)
          .clickable(onClick = onFavoritesClicked, enabled = loaded)
          .padding(4.dp)
          .align(Alignment.TopStart),
      imageVector = if (isFavourite) Icons.Rounded.Star else Icons.Outlined.StarBorder,
      contentDescription = null,
      tint = iconTint,
    )

    Icon(
      modifier =
        Modifier.padding(16.dp)
          .size(32.dp)
          .background(color = topActionIconBg, shape = MaterialTheme.shapes.small)
          .clickable(onClick = championSkins::toggleSkin, enabled = loaded)
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
        modifier = Modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = championName, style = MaterialTheme.typography.displaySmall)
      }
      Text(
        text = championTitle,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Light),
      )

      Text(
        modifier = Modifier.padding(top = 4.dp),
        text = rememberLabeledString(championSkins.imageInfo?.skinName),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Light),
      )
    }
  }
}

@Composable
fun ChampionPartyType(modifier: Modifier, header: String, partyType: PartyType) {
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
            .drawBehind {
              drawRoundRect(color = partyType.color, cornerRadius = CornerRadius(8.dp.toPx()))
            }
            .padding(2.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          modifier = Modifier.widthIn(min = 40.dp),
          text = partyType.label,
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

@Composable
private fun rememberLabeledString(skinName: String?): AnnotatedString {
  val prefix = stringResource(R.string.champion_skin_prefix)
  val noSkin = stringResource(R.string.champion_skin_unavailable)

  return remember(skinName) {
    buildAnnotatedString {
      withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(prefix) }
      append(" ")
      append(skinName ?: noSkin)
    }
  }
}
