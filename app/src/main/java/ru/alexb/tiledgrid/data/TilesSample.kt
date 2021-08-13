package ru.alexb.tiledgrid.data

import ru.alexb.tiledgrid.ui.grid.Tile
import ru.alexb.tiledgrid.ui.theme.Indigo100
import ru.alexb.tiledgrid.ui.theme.Purple100
import ru.alexb.tiledgrid.ui.theme.Teal100

val tilesSample = listOf(
    Tile(id = "1", row = 0, column = 0, width = 2, height = 4, bgColor = Indigo100, badge = true),
    Tile(id = "2", row = 0, column = 2, width = 4, height = 2, bgColor = Purple100),
    Tile(id = "3", row = 2, column = 2, width = 2, height = 2, bgColor = Teal100, badge = true),
    Tile(id = "4", row = 2, column = 4, width = 2, height = 2, bgColor = Indigo100),
    Tile(id = "5", row = 4, column = 0, width = 3, height = 2, bgColor = Teal100),
    Tile(id = "6", row = 4, column = 3, width = 3, height = 2, bgColor = Purple100, badge = true),
)
