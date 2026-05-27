package com.companion.lol.app.ui.screens.championList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorMaxDistance
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.companion.lol.app.R

@Composable
fun ChampionListPullRefreshBox(
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier,
  state: PullToRefreshState = rememberPullToRefreshState(),
  contentAlignment: Alignment = Alignment.TopStart,
  indicatorContentPadding: PaddingValues = PaddingValues.Zero,
  content: @Composable BoxScope.() -> Unit,
) {
  val indicatorHeight = 150.dp
  val maxDistance = IndicatorMaxDistance

  val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
  val animatable = rememberLottieAnimatable()

  LaunchedEffect(composition, isRefreshing) {
    if (isRefreshing) {
      animatable.animate(composition = composition, iterations = LottieConstants.IterateForever)
    } else {
      animatable.snapTo(composition = composition, progress = 0f)
    }
  }

  Box(
    modifier =
      modifier.pullToRefresh(state = state, isRefreshing = isRefreshing, onRefresh = onRefresh),
    contentAlignment = contentAlignment,
  ) {
    Box(
      modifier =
        Modifier.fillMaxWidth().graphicsLayer {
          translationY = state.distanceFraction * maxDistance.toPx()
          clip = true
        }
    ) {
      content()
    }
    IndicatorBox(
      modifier =
        Modifier.padding(indicatorContentPadding)
          .fillMaxWidth()
          .height(indicatorHeight)
          .align(Alignment.TopCenter),
      state = state,
      isRefreshing = isRefreshing,
      containerColor = Color.Transparent,
      maxDistance = maxDistance,
      shape = RectangleShape,
      elevation = 0.dp,
    ) {
      LottieAnimation(
        modifier = Modifier,
        composition = composition,
        progress = { animatable.value },
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth,
      )
    }
  }
}
