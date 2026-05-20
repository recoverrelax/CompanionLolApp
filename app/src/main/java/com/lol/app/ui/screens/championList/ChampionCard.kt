package com.lol.app.ui.screens.championList

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.companion.lol.data.DdragonImage
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.util.ChampionColorCache
import com.lol.app.util.DominantColorCoilImage
import com.lol.app.util.LocalChampionColorCache

@Composable
fun ChampionCard(
  modifier: Modifier,
  champion: ChampionModel,
  gridSize: Int,
  onCardClick: (ChampionId) -> Unit,
) {

  val championColorCache: ChampionColorCache = LocalChampionColorCache.current

  val small =
    MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Light)
  val medium =
    MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.Light)
  val large =
    MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Light)

  val itemId = remember { derivedStateOf { champion.id } }
  val textStyle =
    when (gridSize) {
      3 -> large
      4 -> medium
      else -> small
    }

  val textPaddingVertical =
    when (gridSize) {
      3 -> 6.dp
      4 -> 4.dp
      else -> 2.dp
    }

  val cardCornerRadius =
    when (gridSize) {
      3 -> 8.dp
      4 -> 6.dp
      else -> 4.dp
    }

  ChampionCard(
    modifier = modifier,
    championId = champion.id,
    championImage = champion.squareImageName,
    championName = champion.name,
    championIsFavorite = champion.isFavorite,
    championColorCache = championColorCache,
    textStyle = textStyle,
    textPaddingVertical = textPaddingVertical,
    cardCornerRadius = cardCornerRadius,
    onClick = { onCardClick(itemId.value) },
  )
}

@Composable
fun ChampionCard(
  modifier: Modifier,
  championId: ChampionId,
  championImage: DdragonImage,
  championName: String,
  championIsFavorite: Boolean,
  championColorCache: ChampionColorCache,
  textStyle: TextStyle,
  textPaddingVertical: Dp,
  cardCornerRadius: Dp = 8.dp,
  onClick: () -> Unit,
) {
  val favoriteBorderBrush =
    Brush.verticalGradient(
      colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent),
      tileMode = TileMode.Mirror,
    )
  val favoriteOverlayBrush =
    Brush.verticalGradient(
      colors = listOf(Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
      tileMode = TileMode.Mirror,
    )

  val textOverlapGradient =
    Brush.verticalGradient(
      colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
      tileMode = TileMode.Mirror,
    )

  Card(
    modifier = modifier.clickable(onClick = onClick),
    shape = RoundedCornerShape(bottomStart = cardCornerRadius, bottomEnd = cardCornerRadius),
  ) {
    OverLapColumn(
      modifier = Modifier.fillMaxWidth(),
      element = {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
          DominantColorCoilImage(
            modifier = Modifier.fillMaxSize(),
            championId = championId,
            image = championImage,
            championColorCache = championColorCache,
            imageModifier =
              Modifier.drawWithContent {
                  drawContent()
                  if (championIsFavorite) {
                    drawRect(favoriteOverlayBrush)
                  }
                }
                .then(
                  if (championIsFavorite)
                    Modifier.border(BorderStroke(2.dp, favoriteBorderBrush), RectangleShape)
                  else Modifier
                ),
            skipUpdateColorCache = false,
          )
        }
      },
      overlap = {
        val animatedDominantColor =
          animateColorAsState(
            targetValue = championColorCache.getColor(championId),
            animationSpec = tween(250),
            label = "",
          )
        Text(
          modifier =
            Modifier.fillMaxWidth()
              .drawBehind {
                drawRect(animatedDominantColor.value)
                drawRect(textOverlapGradient)
              }
              .offset(y = (-2).dp)
              .padding(vertical = textPaddingVertical),
          text = championName,
          color = MaterialTheme.colorScheme.onBackground,
          style = textStyle,
          textAlign = TextAlign.Center,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      },
    )
  }
}

@Composable
private fun OverLapColumn(
  modifier: Modifier,
  element: @Composable () -> Unit,
  overlap: @Composable () -> Unit,
  overlapAmount: Dp = 10.dp,
) {
  Layout(
    modifier = modifier,
    content = {
      element()
      overlap()
    },
  ) { measurables: List<Measurable>, constraints: Constraints ->
    val elementPlaceable = measurables[0].measure(constraints)
    val overlapPlaceable = measurables[1].measure(constraints)
    val overlapAmountPx = overlapAmount.roundToPx()

    val width = maxOf(elementPlaceable.width, overlapPlaceable.width)
    val height = elementPlaceable.height + overlapPlaceable.height - overlapAmountPx

    layout(width = width, height = height) {
      elementPlaceable.place(0, 0)
      overlapPlaceable.place(0, elementPlaceable.height - overlapAmountPx)
    }
  }
}
