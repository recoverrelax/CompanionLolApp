package com.companion.lol.app.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.Bitmap
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.Size
import coil3.transform.Transformation
import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.storage.impl.model.ids.ChampionId

private val noPainter = ColorPainter(Color.Transparent)

@Composable
fun DominantColorCoilImage(
  modifier: Modifier,
  championId: ChampionId,
  image: DdragonImage?,
  championColorCache: ChampionColorCache,
  imageModifier: Modifier = Modifier,
  shouldExtractColor: Boolean,
) {
  val context = LocalContext.current

  val painter =
    if (image == null) {
      noPainter
    } else {
      rememberAsyncImagePainter(
        model =
          ImageRequest.Builder(context)
            .data(image.imageUrl)
            .transformations(
              DominantColorTransformation(
                championId = championId,
                championColorCache = championColorCache,
                shouldExtractColor = shouldExtractColor,
              )
            )
            .build()
      )
    }

  val animatedColor: State<Color> =
    animateColorAsState(
      targetValue = championColorCache.getColor(championId),
      label = "",
      animationSpec = tween(250),
    )

  Box(modifier = modifier.drawBehind { drawRect(animatedColor.value) }) {
    Image(
      modifier = Modifier.fillMaxSize().then(imageModifier),
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      alignment = Alignment.Center,
    )
  }
}

private data class DominantColorTransformation(
  private val championId: ChampionId,
  private val championColorCache: ChampionColorCache,
  private val shouldExtractColor: Boolean,
) : Transformation() {
  override val cacheKey: String = "dominantColorTransformation-$$shouldExtractColor"

  override suspend fun transform(input: Bitmap, size: Size): Bitmap {
    if (shouldExtractColor) {
      championColorCache.extractColor(input, championId)
    }
    return input
  }
}
