package ru.alexb.tiledgrid.ui.grid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import ru.alexb.tiledgrid.R
import ru.alexb.tiledgrid.ui.surface.ColoredSurface
import ru.alexb.tiledgrid.ui.surface.GradientSurface
import ru.alexb.tiledgrid.ui.surface.ImageSurface
import ru.alexb.tiledgrid.ui.theme.Amber100
import ru.alexb.tiledgrid.ui.theme.Blue300
import ru.alexb.tiledgrid.ui.theme.BlueGray300
import ru.alexb.tiledgrid.ui.theme.BlueGray50
import ru.alexb.tiledgrid.ui.theme.CinnabarToxic
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
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme
import ru.alexb.tiledgrid.ui.theme.Yellow200
import kotlin.math.PI
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
        is TileBackground.Image -> ImageSurface(
            modifier = modifier.background(color = tile.background.baseColor),
            imageId = tile.background.imageId,
            scale = tile.background.scale,
            alpha = tile.background.alpha,
            offsetX = tile.background.offsetX,
            offsetY = tile.background.offsetY,
            content = tile.content
        )
    }
}

private class TileId(val id: String)

private class BadgeId(val id: String)

@Suppress("unused")
private class Rectangle(val x: Int, val y: Int, val w: Int, val h: Int)

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

val tilesSample = listOf(
    Tile(
        id = "1",
        row = 0,
        column = 0,
        width = 2,
        height = 4,
        background = TileBackground.Colored(Indigo100),
        badge = true
    ),
    Tile(
        id = "2",
        row = 0,
        column = 2,
        width = 4,
        height = 2,
        background = TileBackground.Gradient(
            colors = listOf(Gray100, Yellow200),
            angleRadians = -PI / 2.0
        )
    ),
    Tile(
        id = "3",
        row = 2,
        column = 2,
        width = 2,
        height = 2,
        background = TileBackground.Gradient(
            colors = listOf(Teal100, Purple100),
            angleRadians = PI
        ),
        badge = true
    ),
    Tile(
        id = "4",
        row = 2,
        column = 4,
        width = 2,
        height = 2,
        background = TileBackground.Gradient(
            colors = listOf(BlueGray50, BlueGray300),
            angleRadians = -3.0 * PI / 4.0
        )
    ),
    Tile(
        id = "5",
        row = 4,
        column = 0,
        width = 3,
        height = 2,
        background = TileBackground.Gradient(
            colors = listOf(LightBlue200, Indigo300, DeepPurple600),
            angleRadians = -2.0
        )
    ),
    Tile(
        id = "6",
        row = 4,
        column = 3,
        width = 3,
        height = 2,
        background = TileBackground.Gradient(
            colors = listOf(Yellow200, Orange300, Red400, DeepPurple500, Indigo400, Blue300, Cyan200),
            angleRadians = 0.5
        ),
        badge = true
    ),
    Tile(
        id = "7",
        row = 6,
        column = 0,
        width = 4,
        height = 3,
        background = TileBackground.Image(
            imageId = R.drawable.lenna,
            scale = 5f,
            offsetX = 10.dp,
            offsetY = 200.dp
        )
    ),
    Tile(
        id = "8",
        row = 6,
        column = 4,
        width = 2,
        height = 3,
        background = TileBackground.Image(
            imageId = R.drawable.cheshire_cat,
            baseColor = Amber100,
            alpha = 0.8f,
            scale = 2.0f,
            offsetX = (-30).dp,
            offsetY = 90.dp
        )
    ),
    Tile(
        id = "9",
        row = 9,
        column = 0,
        width = 6,
        height = 4,
        background = TileBackground.Image(
            imageId = R.drawable.stalin,
            baseColor = Gray100,
            scale = 1.5f,
            offsetX = 100.dp,
            offsetY = 80.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(start = 12.dp, top = 11.dp)
        ) {
            Text(
                text = "Работать так,",
                style = MaterialTheme.typography.h4
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "чтобы товарищ Сталин\nспасибо сказал",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    },
)
