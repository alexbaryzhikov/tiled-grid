package ru.alexb.tiledgrid.ui.surface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexb.tiledgrid.ui.theme.Indigo400
import ru.alexb.tiledgrid.ui.theme.LightBlue200
import ru.alexb.tiledgrid.ui.theme.Purple500
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun GradientSurface(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(LightBlue200, Indigo400, Purple500),
    angleRadians: Double = PI,
    content: @Composable () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        val (startOffset, endOffset) = getGradientVector(angleRadians)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = colors,
                        start = startOffset,
                        end = endOffset
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.getGradientVector(angleRadians: Double): Pair<Offset, Offset> {
    val w = maxWidth.value.toDouble()
    val h = maxHeight.value.toDouble()
    val alpha = angleRadians.toLowerRightQuarter()
    val beta = atan2(h, w)
    val gamma = 2 * alpha - beta
    val r = sqrt(w * w + h * h) / 4.0
    val xNorm = (r * (3.0 * cos(beta) + cos(gamma))).round4().dp
    val yNorm = (r * (3.0 * sin(beta) + sin(gamma))).round4().dp
    val (x, y) = when (Quarter.forAngle(angleRadians)) {
        Quarter.LOWER_RIGHT -> xNorm to yNorm
        Quarter.UPPER_RIGHT -> xNorm to (maxHeight - yNorm)
        Quarter.LOWER_LEFT -> (maxWidth - xNorm) to yNorm
        Quarter.UPPER_LEFT -> (maxWidth - xNorm) to (maxHeight - yNorm)
    }
    val (startOffset, endOffset) = with(LocalDensity.current) {
        Offset(x.toPx(), y.toPx()) to Offset((maxWidth - x).toPx(), (maxHeight - y).toPx())
    }
    return startOffset to endOffset
}

private enum class Quarter {
    LOWER_RIGHT,
    LOWER_LEFT,
    UPPER_RIGHT,
    UPPER_LEFT;

    companion object {
        fun forAngle(a: Double): Quarter = when (a.normalizedAngle()) {
            in 0.0..(PI / 2.0) -> LOWER_RIGHT
            in (PI / 2.0)..PI -> LOWER_LEFT
            in -(PI / 2.0)..0.0 -> UPPER_RIGHT
            else -> UPPER_LEFT
        }
    }
}

private fun Double.toLowerRightQuarter(): Double = when (Quarter.forAngle(this)) {
    Quarter.LOWER_RIGHT -> normalizedAngle()
    Quarter.LOWER_LEFT -> PI - normalizedAngle()
    Quarter.UPPER_RIGHT -> -normalizedAngle()
    Quarter.UPPER_LEFT -> PI + normalizedAngle()
}

/** Normalize angle to -PI..PI range. */
private fun Double.normalizedAngle(): Double {
    val a = this % (2.0 * PI)
    return when {
        a > PI -> a - 2.0 * PI
        a < -PI -> a + 2.0 * PI
        else -> a
    }
}

/** Round to 4 places after decimal point. */
private fun Double.round4(): Double = round(this * 1E+4) / 1E+4

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun GradientSurfacePreview() {
    TiledGridTheme {
        GradientSurface(angleRadians = -2.0)
    }
}
