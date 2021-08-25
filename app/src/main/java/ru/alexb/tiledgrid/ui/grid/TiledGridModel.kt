package ru.alexb.tiledgrid.ui.grid

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Tile(
    val id: String,
    val row: Int,
    val column: Int,
    val width: Int,
    val height: Int,
    val background: TileBackground = TileBackground.Colored(Color.White),
    val badge: Boolean = false,
    val content: @Composable () -> Unit = {}
)

sealed class TileBackground {
    class Colored(val color: Color) : TileBackground()

    class Gradient(val colors: List<Color>, val angleRadians: Double) : TileBackground()

    class Image(
        @DrawableRes val imageId: Int,
        val baseColor: Color = Color.White,
        val scale: Float = 1f,
        val alpha: Float = 1f,
        val offsetX: Dp = 0.dp,
        val offsetY: Dp = 0.dp,
    ) : TileBackground()
}
