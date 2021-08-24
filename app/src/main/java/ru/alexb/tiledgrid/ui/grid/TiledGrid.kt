package ru.alexb.tiledgrid.ui.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alexb.tiledgrid.data.tilesSample
import ru.alexb.tiledgrid.ui.surface.ColoredSurface
import ru.alexb.tiledgrid.ui.surface.GradientSurface
import ru.alexb.tiledgrid.ui.theme.CinnabarToxic
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
    badgeProtrusion: Dp = 4.dp,
) {
    Layout(
        content = {
            tiles.forEach {
                Tile(it)
                if (it.badge) Badge(it.id)
            }
        },
        modifier = modifier.padding(badgeProtrusion)
    ) { measurables, constraints ->
        val tilesMap = tiles.associateBy { it.id }
        val resolvedSpanWidth = when (spanWidth) {
            0.dp -> getSpanWidth(constraints.maxWidth.toDp(), spanInterval, columns)
            else -> spanWidth
        }
        var maxHeight = 0
        val tileRectangles = hashMapOf<String, Rectangle>()
        val tilePlaceables = hashMapOf<String, Placeable>()
        val badgePlaceables = hashMapOf<String, Placeable>()
        measurables.forEach { measurable ->
            when (val elementId = measurable.layoutId) {
                is TileId -> {
                    val tile = tilesMap.getValue(elementId.id)
                    val x = getTilePosition(tile.column, resolvedSpanWidth, spanInterval).roundToPx()
                    val y = getTilePosition(tile.row, spanHeight, spanInterval).roundToPx()
                    val width = getTileSize(tile.width, resolvedSpanWidth, spanInterval).roundToPx()
                    val height = getTileSize(tile.height, spanHeight, spanInterval).roundToPx()
                    maxHeight = max(maxHeight, y + height)
                    tileRectangles[elementId.id] = Rectangle(x, y, width, height)
                    tilePlaceables[elementId.id] = measurable.measure(
                        constraints.copy(
                            minWidth = width,
                            maxWidth = width,
                            minHeight = height,
                            maxHeight = height,
                        )
                    )
                }
                is BadgeId -> {
                    badgePlaceables[elementId.id] = measurable.measure(
                        constraints.copy(
                            minWidth = 0,
                            minHeight = 0
                        )
                    )
                }
            }
        }
        layout(constraints.maxWidth, maxHeight) {
            tilePlaceables.forEach { (id, placeable) ->
                val rect = tileRectangles.getValue(id)
                placeable.placeRelative(
                    x = rect.x,
                    y = rect.y
                )
            }
            badgePlaceables.forEach { (id, placeable) ->
                val rect = tileRectangles.getValue(id)
                placeable.placeRelative(
                    x = rect.x + rect.w + badgeProtrusion.roundToPx() - placeable.width,
                    y = rect.y - badgeProtrusion.roundToPx()
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
    val modifier = Modifier
        .layoutId(TileId(tile.id))
        .clip(MaterialTheme.shapes.medium)
        .clickable {}
    when (tile.background) {
        is TileBackground.Colored -> ColoredSurface(
            modifier = modifier,
            color = tile.background.color,
            content = tile.content
        )
        is TileBackground.Gradient -> GradientSurface(
            modifier = modifier,
            colors = tile.background.colors,
            angleRadians = tile.background.angleRadians,
            content = tile.content
        )
    }
}

@Composable
private fun Badge(id: String) {
    Surface(
        modifier = Modifier.layoutId(BadgeId(id)),
        shape = RoundedCornerShape(100.dp),
        color = CinnabarToxic,
        elevation = 8.dp
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(18.dp),
            text = "New",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
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
