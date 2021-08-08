package ru.alexb.tiledgrid.ui.grid

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import ru.alexb.tiledgrid.data.TilePosition
import ru.alexb.tiledgrid.data.TileAdapter
import ru.alexb.tiledgrid.data.TileDto

class TileAdapterTest {
    private lateinit var tileAdapter: TileAdapter

    @Before
    fun before() {
        tileAdapter = TileAdapter()
    }

    @Test
    fun should_return_empty_list_on_empty_tiles() {
        val result = tileAdapter.getTilePositions(1, emptyList())
        assertThat(result).isEmpty()
    }

    // 1
    @Test
    fun should_place_a_single_tile() {
        val tiles = listOf(TileDto("1", 1, 1))
        val result = tileAdapter.getTilePositions(1, tiles)
        assertThat(result).containsExactly(TilePosition(0, 0))
    }

    // 1 2
    @Test
    fun should_place_two_tiles_on_the_same_row() {
        val tiles = listOf(
            TileDto("1", 1, 1),
            TileDto("2", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 1)
        )
    }

    // 1 1 2
    @Test
    fun should_place_two_tiles_on_the_same_row_2() {
        val tiles = listOf(
            TileDto("1", 2, 1),
            TileDto("2", 1, 1)
        )
        val result = tileAdapter.getTilePositions(3, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 2)
        )
    }

    // 1
    // 2
    @Test
    fun should_place_two_tiles_on_two_rows() {
        val tiles = listOf(
            TileDto("1", 1, 1),
            TileDto("2", 1, 1)
        )
        val result = tileAdapter.getTilePositions(1, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(1, 0)
        )
    }


    // 1 .
    // 2 2
    @Test
    fun should_place_two_tiles_on_two_rows_2() {
        val tiles = listOf(
            TileDto("1", 1, 1),
            TileDto("2", 2, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(1, 0)
        )
    }

    // 1 2
    // 1 3
    @Test
    fun should_place_three_tiles() {
        val tiles = listOf(
            TileDto("1", 1, 2),
            TileDto("2", 1, 1),
            TileDto("3", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 1),
            TilePosition(1, 1)
        )
    }

    // 1 2
    // 3 2
    @Test
    fun should_place_three_tiles_2() {
        val tiles = listOf(
            TileDto("1", 1, 1),
            TileDto("2", 1, 2),
            TileDto("3", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 1),
            TilePosition(0, 1)
        )
    }

    // 1 5
    @Test
    fun should_return_invalid_positions_for_tiles_with_zero_dimension() {
        val tiles = listOf(
            TileDto("1", 1, 1),
            TileDto("2", 0, 1),
            TileDto("3", 1, 0),
            TileDto("4", 0, 0),
            TileDto("5", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition.INVALID,
            TilePosition.INVALID,
            TilePosition.INVALID,
            TilePosition(0, 1)
        )
    }

    // 1 3
    @Test
    fun should_return_invalid_positions_for_too_wide_tiles() {
        val tiles = listOf(
            TileDto("1", 1, 1),
            TileDto("2", 3, 1),
            TileDto("3", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition.INVALID,
            TilePosition(0, 1)
        )
    }

    // 1 1 1 1 2 2
    // 3 3 4 4 4 4
    // 5 5 5 6 6 6
    @Test
    fun should_place_multiple_tiles() {
        val tiles = listOf(
            TileDto("1", 4, 1),
            TileDto("2", 2, 1),
            TileDto("3", 2, 1),
            TileDto("4", 4, 1),
            TileDto("5", 3, 1),
            TileDto("6", 3, 1),
        )
        val result = tileAdapter.getTilePositions(6, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 4),
            TilePosition(1, 0),
            TilePosition(1, 2),
            TilePosition(2, 0),
            TilePosition(2, 3),
        )
    }

    // 1 2 3
    // 1 2 3
    // 1 4 3
    // 1 4 5
    // 6 4 5
    // 6 4 5
    @Test
    fun should_place_multiple_tiles_2() {
        val tiles = listOf(
            TileDto("1", 1, 4),
            TileDto("2", 1, 2),
            TileDto("3", 1, 3),
            TileDto("4", 1, 4),
            TileDto("5", 1, 3),
            TileDto("6", 1, 2),
        )
        val result = tileAdapter.getTilePositions(3, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 1),
            TilePosition(0, 2),
            TilePosition(2, 1),
            TilePosition(3, 2),
            TilePosition(4, 0),
        )
    }

    // 1 1 2 2 .
    // 1 1 3 3 3
    // 1 1 3 3 3
    // . . 3 3 3
    // 4 4 4 5 5
    // 4 4 4 5 5
    @Test
    fun should_place_multiple_tiles_3() {
        val tiles = listOf(
            TileDto("1", 2, 3),
            TileDto("2", 2, 1),
            TileDto("3", 3, 3),
            TileDto("4", 3, 2),
            TileDto("5", 2, 2),
        )
        val result = tileAdapter.getTilePositions(5, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0),
            TilePosition(0, 2),
            TilePosition(1, 2),
            TilePosition(4, 0),
            TilePosition(4, 3),
        )
    }

    // 1 1 1 9 7 7 7
    // a 2 2 2 7 7 7
    // b b 3 3 3 c c
    // 8 8 8 4 4 4 d
    // 8 8 8 e 5 5 5
    // 8 8 8 6 6 6 f
    @Test
    fun should_place_multiple_tiles_4() {
        val tiles = listOf(
            TileDto("1", 3, 1),
            TileDto("9", 1, 1),
            TileDto("7", 3, 2),
            TileDto("a", 1, 1),
            TileDto("2", 3, 1),
            TileDto("b", 2, 1),
            TileDto("3", 3, 1),
            TileDto("c", 2, 1),
            TileDto("8", 3, 3),
            TileDto("4", 3, 1),
            TileDto("d", 1, 1),
            TileDto("e", 1, 1),
            TileDto("5", 3, 1),
            TileDto("6", 3, 1),
            TileDto("f", 1, 1),
        )
        val result = tileAdapter.getTilePositions(7, tiles)
        assertThat(result).containsExactly(
            TilePosition(0, 0), // 1
            TilePosition(0, 3), // 9
            TilePosition(0, 4), // 7
            TilePosition(1, 0), // a
            TilePosition(1, 1), // 2
            TilePosition(2, 0), // b
            TilePosition(2, 2), // 3
            TilePosition(2, 5), // c
            TilePosition(3, 0), // 8
            TilePosition(3, 3), // 4
            TilePosition(3, 6), // d
            TilePosition(4, 3), // e
            TilePosition(4, 4), // 5
            TilePosition(5, 3), // 6
            TilePosition(5, 6), // f
        )
    }
}