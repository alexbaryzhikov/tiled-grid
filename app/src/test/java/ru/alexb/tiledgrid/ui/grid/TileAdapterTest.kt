package ru.alexb.tiledgrid.ui.grid

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

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
        val tiles = listOf(Tile("1", 1, 1))
        val result = tileAdapter.getTilePositions(1, tiles)
        assertThat(result).containsExactly(Position(0, 0))
    }

    // 1 2
    @Test
    fun should_place_two_tiles_on_the_same_row() {
        val tiles = listOf(
            Tile("1", 1, 1),
            Tile("2", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(1, 0)
        )
    }

    // 1 1 2
    @Test
    fun should_place_two_tiles_on_the_same_row_2() {
        val tiles = listOf(
            Tile("1", 2, 1),
            Tile("2", 1, 1)
        )
        val result = tileAdapter.getTilePositions(3, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(2, 0)
        )
    }

    // 1
    // 2
    @Test
    fun should_place_two_tiles_on_two_rows() {
        val tiles = listOf(
            Tile("1", 1, 1),
            Tile("2", 1, 1)
        )
        val result = tileAdapter.getTilePositions(1, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(0, 1)
        )
    }


    // 1 .
    // 2 2
    @Test
    fun should_place_two_tiles_on_two_rows_2() {
        val tiles = listOf(
            Tile("1", 1, 1),
            Tile("2", 2, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(0, 1)
        )
    }

    // 1 2
    // 1 3
    @Test
    fun should_place_three_tiles() {
        val tiles = listOf(
            Tile("1", 1, 2),
            Tile("2", 1, 1),
            Tile("3", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(1, 0),
            Position(1, 1)
        )
    }

    // 1 2
    // 3 2
    @Test
    fun should_place_three_tiles_2() {
        val tiles = listOf(
            Tile("1", 1, 1),
            Tile("2", 1, 2),
            Tile("3", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(1, 0),
            Position(1, 0)
        )
    }

    // 1 5
    @Test
    fun should_return_invalid_positions_for_tiles_with_zero_dimension() {
        val tiles = listOf(
            Tile("1", 1, 1),
            Tile("2", 0, 1),
            Tile("3", 1, 0),
            Tile("4", 0, 0),
            Tile("5", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position.INVALID,
            Position.INVALID,
            Position.INVALID,
            Position(1, 0)
        )
    }

    // 1 3
    @Test
    fun should_return_invalid_positions_for_too_wide_tiles() {
        val tiles = listOf(
            Tile("1", 1, 1),
            Tile("2", 3, 1),
            Tile("3", 1, 1)
        )
        val result = tileAdapter.getTilePositions(2, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position.INVALID,
            Position(1, 0)
        )
    }

    // 1 1 1 1 2 2
    // 3 3 4 4 4 4
    // 5 5 5 6 6 6
    @Test
    fun should_place_multiple_tiles() {
        val tiles = listOf(
            Tile("1", 4, 1),
            Tile("2", 2, 1),
            Tile("3", 2, 1),
            Tile("4", 4, 1),
            Tile("5", 3, 1),
            Tile("6", 3, 1),
        )
        val result = tileAdapter.getTilePositions(6, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(4, 0),
            Position(0, 1),
            Position(2, 1),
            Position(0, 2),
            Position(3, 2),
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
            Tile("1", 1, 4),
            Tile("2", 1, 2),
            Tile("3", 1, 3),
            Tile("4", 1, 4),
            Tile("5", 1, 3),
            Tile("6", 1, 2),
        )
        val result = tileAdapter.getTilePositions(3, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(1, 0),
            Position(2, 0),
            Position(1, 2),
            Position(2, 3),
            Position(0, 4),
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
            Tile("1", 2, 3),
            Tile("2", 2, 1),
            Tile("3", 3, 3),
            Tile("4", 3, 2),
            Tile("5", 2, 2),
        )
        val result = tileAdapter.getTilePositions(5, tiles)
        assertThat(result).containsExactly(
            Position(0, 0),
            Position(2, 0),
            Position(2, 1),
            Position(0, 4),
            Position(3, 4),
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
            Tile("1", 3, 1),
            Tile("9", 1, 1),
            Tile("7", 3, 2),
            Tile("a", 1, 1),
            Tile("2", 3, 1),
            Tile("b", 2, 1),
            Tile("3", 3, 1),
            Tile("c", 2, 1),
            Tile("8", 3, 3),
            Tile("4", 3, 1),
            Tile("d", 1, 1),
            Tile("e", 1, 1),
            Tile("5", 3, 1),
            Tile("6", 3, 1),
            Tile("f", 1, 1),
        )
        val result = tileAdapter.getTilePositions(7, tiles)
        assertThat(result).containsExactly(
            Position(0, 0), // 1
            Position(3, 0), // 9
            Position(4, 0), // 7
            Position(0, 1), // a
            Position(1, 1), // 2
            Position(0, 2), // b
            Position(2, 2), // 3
            Position(5, 2), // c
            Position(0, 3), // 8
            Position(3, 3), // 4
            Position(6, 3), // d
            Position(3, 4), // e
            Position(4, 4), // 5
            Position(3, 5), // 6
            Position(6, 5), // f
        )
    }
}