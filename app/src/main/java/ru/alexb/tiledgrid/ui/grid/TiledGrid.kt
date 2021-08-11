package ru.alexb.tiledgrid.ui.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import ru.alexb.tiledgrid.ui.theme.Purple200
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme
import kotlin.math.max

@Composable
fun TiledGrid(
    modifier: Modifier = Modifier,
    horizontalSpans: Int = 6,
    interval: Dp = 8.dp,
    tiles: Flow<List<Tile>> = emptyFlow(),
) {
    require(horizontalSpans > 0) { "Horizontal spans must be greater than 0" }
    val tilesState by tiles.collectAsState(initial = emptyList())
    Layout(
        content = { tilesState.forEach { Tile(it) } },
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

private fun getTilePosition(spans: Int, dpu: Dp, interval: Dp): Dp = (dpu + interval) * spans

private fun getTileSize(spans: Int, dpu: Dp, interval: Dp): Dp = dpu * spans + interval * (spans - 1)

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

@Composable
fun MyList(
    modifier: Modifier = Modifier
) {
    val spans by remember { mutableStateOf(3) }
    Layout(
        modifier = modifier,
        content = {
            repeat(spans) {
                Surface(color = Purple200) { }
            }
        }
    ) { measurables, constraints ->
        var height = 0
        val placeables = measurables.map {
            it.measure(constraints.copy(minHeight = 10.dp.roundToPx()))
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach {
                it.placeRelative(
                    x = 0,
                    y = height
                )
                height += it.height + 8.dp.roundToPx()
            }
        }
    }
}

@Preview()
@Composable
fun MyListPreview() {
    MyList(modifier = Modifier.size(100.dp))
}

@Preview
@Composable
fun TilePreview() {
    TiledGridTheme {
        Tile(Tile(id = "1", row = 0, column = 0, width = 2, height = 2, bgColor = Purple200) {
            Surface(modifier = Modifier.size(80.dp), color = Color(0x00000000)) {}
        })
    }
}

@Preview
@Composable
fun TiledGridPreview() {
    TiledGridTheme {
        Surface(color = MaterialTheme.colors.background) {
            TiledGrid(
                modifier = Modifier.size(300.dp),
                horizontalSpans = 6,
                tiles = flowOf(tilesSample)
            )
        }
    }
}
