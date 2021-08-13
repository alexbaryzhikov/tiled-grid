package ru.alexb.tiledgrid.ui.grid

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.alexb.tiledgrid.data.tilesSample
import ru.alexb.tiledgrid.ui.theme.Indigo400
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme
import kotlin.math.max

@Composable
fun TiledGrid(
    tiles: List<Tile>,
    modifier: Modifier = Modifier,
    columns: Int = 6,
    spanWidth: Dp = 0.dp,
    spanHeight: Dp = 48.dp,
    spanInterval: Dp = 8.dp,
) {
    Layout(
        content = { tiles.forEach { Tile(it) } },
        modifier = modifier
    ) { measurables, constraints ->
        val tilesMap = tiles.associateBy { it.id }
        val resolvedSpanWidth = when (spanWidth) {
            0.dp -> getSpanWidth(constraints.maxWidth.toDp(), spanInterval, columns)
            else -> spanWidth
        }
        var maxHeight = 0
        val positions = hashMapOf<String, Pair<Int, Int>>()
        val placeables = hashMapOf<String, Placeable>()
        measurables.forEach { measurable ->
            val id = measurable.layoutId as String
            val tile = tilesMap.getValue(id)
            val x = getTilePosition(tile.column, resolvedSpanWidth, spanInterval).roundToPx()
            val y = getTilePosition(tile.row, spanHeight, spanInterval).roundToPx()
            val width = getTileSize(tile.width, resolvedSpanWidth, spanInterval).roundToPx()
            val height = getTileSize(tile.height, spanHeight, spanInterval).roundToPx()
            maxHeight = max(maxHeight, y + height)
            positions[id] = Pair(x, y)
            placeables[id] = measurable.measure(
                constraints.copy(
                    minWidth = width,
                    maxWidth = width,
                    minHeight = height,
                    maxHeight = height,
                )
            )
        }
        layout(constraints.maxWidth, maxHeight) {
            placeables.forEach { (id, placeable) ->
                val position = positions.getValue(id)
                placeable.placeRelative(
                    x = position.first,
                    y = position.second
                )
            }
        }
    }
}

private fun getSpanWidth(maxWidth: Dp, spanInterval: Dp, columns: Int): Dp =
    (maxWidth - spanInterval * (columns - 1)) / columns

private fun getTilePosition(spans: Int, spanSize: Dp, spanInterval: Dp): Dp =
    (spanSize + spanInterval) * spans

private fun getTileSize(spans: Int, spanSize: Dp, interval: Dp): Dp =
    spanSize * spans + interval * (spans - 1)

@Composable
private fun Tile(tile: Tile) {
    Surface(
        modifier = Modifier
            .layoutId(tile.id)
            .clip(MaterialTheme.shapes.medium)
            .clickable {},
        color = tile.bgColor,
        content = tile.content
    )
}

@Preview(widthDp = 100, heightDp = 100)
@Composable
fun TilePreview() {
    TiledGridTheme {
        Tile(Tile(
            id = "1",
            row = 0,
            column = 0,
            width = 2,
            height = 2,
            bgColor = Indigo400
        ))
    }
}

@Preview(widthDp = 400)
@Composable
fun TiledGridPreview() {
    TiledGridTheme {
        Surface(color = MaterialTheme.colors.background) {
            TiledGrid(
                tiles = tilesSample,
                columns = 6
            )
        }
    }
}
