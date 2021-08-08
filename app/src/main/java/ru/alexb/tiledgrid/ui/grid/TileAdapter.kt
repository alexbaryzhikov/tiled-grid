package ru.alexb.tiledgrid.ui.grid

import java.util.ArrayList

class TileAdapter {

    fun getTilePositions(width: Int, tiles: List<Tile>): List<Position> {
        if (tiles.isEmpty()) return emptyList()
        val grid = arrayListOf(BooleanArray(width))
        return tiles.map { tile -> placeTile(grid, tile) }
    }

    private fun placeTile(grid: ArrayList<BooleanArray>, tile: Tile): Position {
        if (tile.width == 0 || tile.height == 0 || tile.width > grid[0].size) {
            return Position.INVALID
        }
        return Position(0, 0)
    }
}