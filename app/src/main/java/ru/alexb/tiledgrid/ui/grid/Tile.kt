package ru.alexb.tiledgrid.ui.grid

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class Tile(
    val id: String,
    val width: Int,
    val height: Int,
    val row: Int,
    val column: Int,
    val bgColor: Color,
    val content: @Composable () -> Unit
)
