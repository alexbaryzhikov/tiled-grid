package ru.alexb.tiledgrid.ui.grid

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
}

sealed class ElementId(val id: String)

class TileId(id: String) : ElementId(id)

class BadgeId(id: String) : ElementId(id)

class Rectangle(val x: Int, val y: Int, val w: Int, val h: Int)
