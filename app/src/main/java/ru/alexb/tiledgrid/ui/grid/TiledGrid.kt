package ru.alexb.tiledgrid.ui.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import ru.alexb.tiledgrid.data.tilesSample
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme
import kotlin.math.max

@Composable
fun TiledGrid(
    modifier: Modifier = Modifier,
    horizontalSpans: Int = 6,
    interval: Dp = 5.dp,
    tiles: Flow<List<Tile>> = emptyFlow(),
) {
    require(horizontalSpans > 0) { "Horizontal spans must be greater than 0" }
    val tilesState by tiles.collectAsState(initial = emptyList())
    Layout(
        content = { tilesState.map { Tile(it) } },
        modifier = modifier
    ) { measurables, constraints ->
        val tilesMap = tilesState.associateBy { it.id }
        val dpu = (constraints.maxWidth.toDp() - interval * (horizontalSpans - 1)) / horizontalSpans
        var maxHeight = 0
        val positions = hashMapOf<String, Pair<Int, Int>>()
        val placeables = hashMapOf<String, Placeable>()
        measurables.forEach { measurable ->
            val id = measurable.layoutId as String
            val tile = tilesMap.getValue(id)
            val x = getTilePosition(tile.column, dpu, interval).roundToPx()
            val y = getTilePosition(tile.row, dpu, interval).roundToPx()
            val width = getTileSize(tile.width, dpu, interval).roundToPx()
            val height = getTileSize(tile.height, dpu, interval).roundToPx()
            maxHeight = max(maxHeight, y + height)
            positions[id] = Pair(x, y)
            placeables[id] = measurable.measure(
                constraints.copy(
                    minWidth = width,
                    maxWidth = width,
                    minHeight = height,
                    maxHeight = height
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

private fun getTileSize(spans: Int, dpu: Dp, interval: Dp): Dp = dpu * spans + interval * (spans - 1)

private fun getTilePosition(spans: Int, dpu: Dp, interval: Dp): Dp = (dpu + interval) * spans

@Composable
private fun Tile(tile: Tile) {
    Surface(
        modifier = Modifier
            .layoutId(tile.id)
            .clip(MaterialTheme.shapes.medium)
            .clickable {},
        color = tile.bgColor,
        content = tile.content,
    )
}

@Preview
@Composable
fun TiledGridPreview() {
    TiledGridTheme(darkTheme = true) {
        TiledGrid(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            horizontalSpans = 6,
            tiles = flowOf(tilesSample)
        )
    }
}