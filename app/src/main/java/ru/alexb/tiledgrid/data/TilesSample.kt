package ru.alexb.tiledgrid.data

import ru.alexb.tiledgrid.ui.grid.Tile
import ru.alexb.tiledgrid.ui.grid.TileBackground
import ru.alexb.tiledgrid.ui.theme.Blue300
import ru.alexb.tiledgrid.ui.theme.BlueGray300
import ru.alexb.tiledgrid.ui.theme.BlueGray50
import ru.alexb.tiledgrid.ui.theme.Cyan200
import ru.alexb.tiledgrid.ui.theme.DeepPurple500
import ru.alexb.tiledgrid.ui.theme.DeepPurple600
import ru.alexb.tiledgrid.ui.theme.Gray100
import ru.alexb.tiledgrid.ui.theme.Indigo100
import ru.alexb.tiledgrid.ui.theme.Indigo300
import ru.alexb.tiledgrid.ui.theme.Indigo400
import ru.alexb.tiledgrid.ui.theme.LightBlue200
import ru.alexb.tiledgrid.ui.theme.Orange300
import ru.alexb.tiledgrid.ui.theme.Purple100
import ru.alexb.tiledgrid.ui.theme.Red400
import ru.alexb.tiledgrid.ui.theme.Teal100
import ru.alexb.tiledgrid.ui.theme.Yellow200
import kotlin.math.PI

val tilesSample = listOf(
    Tile(id = "1", row = 0, column = 0, width = 2, height = 4, background = TileBackground.Colored(Indigo100), badge = true),
    Tile(id = "2", row = 0, column = 2, width = 4, height = 2, background = TileBackground.Gradient(listOf(Gray100, Yellow200), -PI / 2.0)),
    Tile(id = "3", row = 2, column = 2, width = 2, height = 2, background = TileBackground.Gradient(listOf(Teal100, Purple100), PI), badge = true),
    Tile(id = "4", row = 2, column = 4, width = 2, height = 2, background = TileBackground.Gradient(listOf(BlueGray50, BlueGray300), -3.0 * PI / 4.0)),
    Tile(id = "5", row = 4, column = 0, width = 3, height = 2, background = TileBackground.Gradient(listOf(LightBlue200, Indigo300, DeepPurple600), -2.0)),
    Tile(id = "6", row = 4, column = 3, width = 3, height = 2, background = TileBackground.Gradient(listOf(Yellow200, Orange300, Red400, DeepPurple500, Indigo400, Blue300, Cyan200), 0.5), badge = true),
)
