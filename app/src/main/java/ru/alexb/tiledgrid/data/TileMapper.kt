package ru.alexb.tiledgrid.data

import ru.alexb.tiledgrid.ui.grid.Tile
import ru.alexb.tiledgrid.ui.theme.Purple200

fun tileFrom(dto: TileDto, position: TilePosition): Tile = Tile(
    id = dto.id,
    width = dto.width,
    height = dto.height,
    row = position.row,
    column = position.column,
    bgColor = Purple200,
    content = {}
)