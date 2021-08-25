package ru.alexb.tiledgrid.ui.surface

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.alexb.tiledgrid.R
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme

@Composable
fun ImageSurface(
    @DrawableRes imageId: Int,
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    alpha: Float = 1f,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    content: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        val (translationX, translationY) = with(LocalDensity.current) { offsetX.toPx() to offsetY.toPx() }
        Image(
            painter = painterResource(id = imageId),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    alpha = alpha,
                    translationX = translationX,
                    translationY = translationY
                ),
            contentDescription = null
        )
        content()
    }
}

@Preview(widthDp = 200, heightDp = 160)
@Composable
fun ImageSurfacePreview() {
    TiledGridTheme {
        ImageSurface(
            imageId = R.drawable.lenna,
            scale = 5f,
            alpha = 1f,
            offsetX = 10.dp,
            offsetY = 200.dp
        )
    }
}
