package ru.alexb.tiledgrid.data

data class TilePosition(
    val row: Int,
    val column: Int,
) {
    companion object {
        val INVALID = TilePosition(-1, -1)
    }
}