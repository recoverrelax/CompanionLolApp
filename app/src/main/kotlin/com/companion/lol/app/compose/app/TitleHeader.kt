package com.companion.lol.app.compose.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun TitleHeader(
  modifier: Modifier,
  borderColor: Color = MaterialTheme.colorScheme.outline,
  contentColor: Color = MaterialTheme.colorScheme.onBackground,
  borderWidth: Dp = 1.dp,
  cornerRadius: Dp = 6.dp,
  label: String,
) {
  Text(
    modifier =
      modifier
        .drawBehind {
          clipRect(right = size.width / 1.1f, top = size.height / 4, clipOp = ClipOp.Intersect) {
            drawRoundRect(
              color = borderColor,
              style = Stroke(width = borderWidth.toPx()),
              cornerRadius = with(cornerRadius.toPx()) { CornerRadius(this, this) },
              topLeft = with(borderWidth.toPx()) { Offset(this, this) },
              size = with((2 * borderWidth).toPx()) { Size(size.width - this, size.height - this) },
            )
          }
        }
        .padding(horizontal = 5.dp, vertical = 4.dp),
    text = label,
    style = MaterialTheme.typography.bodyMedium,
    color = contentColor,
  )
}
