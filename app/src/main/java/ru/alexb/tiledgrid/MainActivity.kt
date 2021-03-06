package ru.alexb.tiledgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.alexb.tiledgrid.ui.grid.TiledGrid
import ru.alexb.tiledgrid.ui.grid.tilesSample
import ru.alexb.tiledgrid.ui.surface.GradientSurface
import ru.alexb.tiledgrid.ui.surface.ImageSurface
import ru.alexb.tiledgrid.ui.theme.TiledGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
    }
}

@Composable
fun MainScreen() {
    TiledGridTheme {
        Surface(color = MaterialTheme.colors.background) {
            TiledGridDemo()
        }
    }
}

@Composable
fun TiledGridDemo() {
    TiledGrid(
        tiles = tilesSample,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        columns = 6
    )
}

@Composable
fun GradientSurfaceDemo() {
    var angle by remember { mutableStateOf(0.0) }
    LaunchedEffect(Unit) {
        launch {
            while (true) {
                delay(10)
                angle += 1.0 / 128.0
            }
        }
    }
    Column {
        Text(angle.toString())
        GradientSurface(
            modifier = Modifier
                .width(200.dp)
                .height(160.dp),
            angleRadians = angle
        )
    }
}

@Composable
fun ImageSurfaceDemo() {
    var offsetY by remember { mutableStateOf(200.dp) }
    LaunchedEffect(Unit) {
        launch {
            while (true) {
                delay(10)
                offsetY -= 0.1.dp
            }
        }
    }
    ImageSurface(
        imageId = R.drawable.lenna,
        modifier = Modifier
            .width(200.dp)
            .height(160.dp),
        scale = 5f,
        alpha = 1f,
        offsetX = 10.dp,
        offsetY = offsetY
    )
}
