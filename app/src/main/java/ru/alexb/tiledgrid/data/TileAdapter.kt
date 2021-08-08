package ru.alexb.tiledgrid.data

import java.util.*

class TileAdapter {

    fun getTilePositions(width: Int, tiles: List<TileDto>): List<TilePosition> {
        if (tiles.isEmpty()) return emptyList()
        val grid = arrayListOf(BooleanArray(width))
        return tiles.map { tile -> placeTile(grid, tile) }
    }

    private fun placeTile(grid: ArrayList<BooleanArray>, tile: TileDto): TilePosition {
        if (tile.width == 0 || tile.height == 0 || tile.width > grid[0].size) {
            return TilePosition.INVALID
        }
        // TODO: implement tile placement
        return TilePosition(0, 0)
    }
}