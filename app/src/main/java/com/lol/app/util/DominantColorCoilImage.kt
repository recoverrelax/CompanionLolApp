package com.lol.app.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.Bitmap
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.Size
import coil3.transform.Transformation
import com.companion.lol.data.DdragonImage
import com.companion.lol.storage.impl.model.ids.ChampionId

@Composable
fun DominantColorCoilImage(
  modifier: Modifier,
  image: DdragonImage,
  championColorCache: ChampionColorCache,
  imageModifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  val painter =
    rememberAsyncImagePainter(
      model =
        ImageRequest.Builder(context)
          .data(image.imageUrl)
          .transformations(
            DominantColorTransformation(
              championId = image.championId,
              championColorCache = championColorCache,
            )
          )
          .build()
    )

  val painterState by painter.state.collectAsState()

  val animatedColor: State<Color> =
    animateColorAsState(
      targetValue =
        when (painterState) {
          is AsyncImagePainter.State.Success -> Color.Transparent
          else -> championColorCache.getColor(image.championId)
        },
      label = "",
      animationSpec = tween(350),
    )

  Box(modifier = modifier.drawBehind { drawRect(animatedColor.value) }) {
    Image(
      modifier = Modifier.fillMaxSize().then(imageModifier),
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.Crop,
    )
  }
}

private class DominantColorTransformation(
  private val championId: ChampionId,
  private val championColorCache: ChampionColorCache,
) : Transformation() {
  override val cacheKey: String = championId.value.toString()

  override suspend fun transform(input: Bitmap, size: Size): Bitmap {
    championColorCache.extractColor(input, championId)
    return input
  }
}
