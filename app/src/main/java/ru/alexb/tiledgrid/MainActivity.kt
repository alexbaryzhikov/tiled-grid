package ru.alexb.tiledgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alexb.tiledgrid.data.tilesSample
import ru.alexb.tiledgrid.ui.grid.TiledGrid
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
            TiledGrid(
                tiles = tilesSample,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                columns = 6
            )
        }
    }
}
