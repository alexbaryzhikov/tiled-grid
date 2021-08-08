package ru.alexb.tiledgrid.ui.grid

data class Tile(
    val id: String,
    val width: Int,
    val height: Int,
)

data class Position(
    val x: Int,
    val y: Int,
) {
    companion object {
        val INVALID = Position(-1, -1)
    }
}