package com.companion.lol.app.ui.screens.championList

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
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
import com.companion.lol.app.util.ChampionColorCache
import com.companion.lol.app.util.DominantColorCoilImage
import com.companion.lol.app.util.LocalChampionColorCache
import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.GridSize

@Composable
fun ChampionCard(
  modifier: Modifier,
  champion: ChampionModel,
  gridSize: GridSize,
  onCardClick: (ChampionId) -> Unit,
) {
  val championColorCache: ChampionColorCache = LocalChampionColorCache.current

  val small =
    MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Light)
  val medium =
    MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.Light)
  val large =
    MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Light)

  val textStyle =
    when (gridSize) {
      GridSize.SMALL -> small
      GridSize.MEDIUM -> medium
      GridSize.LARGE -> large
    }

  val textPaddingVertical =
    when (gridSize) {
      GridSize.SMALL -> 2.dp
      GridSize.MEDIUM -> 4.dp
      GridSize.LARGE -> 6.dp
    }

  val cardCornerRadius =
    with(
      when (gridSize) {
        GridSize.SMALL -> 4.dp
        GridSize.MEDIUM -> 6.dp
        GridSize.LARGE -> 8.dp
      }
    ) {
      RoundedCornerShape(bottomStart = this, bottomEnd = this)
    }

  ChampionCard(
    modifier = modifier,
    championId = champion.id,
    squareImage = champion.squareImage,
    championName = champion.name,
    championIsFavorite = champion.isFavorite,
    championColorCache = championColorCache,
    textStyle = textStyle,
    textPaddingVertical = textPaddingVertical,
    shape = cardCornerRadius,
    onClick = onCardClick,
  )
}

@Composable
fun ChampionCard(
  modifier: Modifier,
  championId: ChampionId,
  squareImage: DdragonImage,
  championName: String,
  championIsFavorite: Boolean,
  championColorCache: ChampionColorCache,
  textStyle: TextStyle,
  textPaddingVertical: Dp,
  shape: Shape,
  onClick: (ChampionId) -> Unit,
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

  Card(modifier = modifier.clickable(onClick = { onClick(championId) }), shape = shape) {
    OverLapColumn(
      modifier = Modifier.fillMaxWidth(),
      element = {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
          DominantColorCoilImage(
            modifier = Modifier.fillMaxSize(),
            championId = championId,
            image = squareImage,
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
            shouldUpdateColor = false,
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
