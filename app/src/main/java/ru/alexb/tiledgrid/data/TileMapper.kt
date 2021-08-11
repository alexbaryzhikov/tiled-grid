package ru.alexb.tiledgrid.data

import ru.alexb.tiledgrid.ui.grid.Tile
import ru.alexb.tiledgrid.ui.theme.Purple200

fun tileFrom(dto: TileDto, position: TilePosition): Tile = Tile(
  id = dto.id,
  row = position.row,
  column = position.column,
  width = dto.width,
  height = dto.height,
  bgColor = Purple200
) {}
