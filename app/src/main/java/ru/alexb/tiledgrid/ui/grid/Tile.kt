package ru.alexb.tiledgrid.ui.grid

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class Tile(
    val id: String,
    val row: Int,
    val column: Int,
    val width: Int,
    val height: Int,
    val bgColor: Color,
    val badge: Boolean = false,
    val content: @Composable () -> Unit = {}
)
