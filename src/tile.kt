
enum class TileColor { BLUE, RED, ORANGE, BLACK }

class Tile(var color: TileColor, var number: Int?, var serial: Int) {
    companion object {
        const val NUMBER_OF_TILES_PER_COLOR = 13
        const val NUMBER_OF_TILES_PER_RACK = 14

        fun getColorRange(): Array<TileColor> = TileColor.values()
        fun getNumberRange(): IntRange = (1..NUMBER_OF_TILES_PER_COLOR)
        fun getRackRange(): IntRange = (1..NUMBER_OF_TILES_PER_RACK)
    }
}

// Tile collections
class Run(var tiles: MutableList<Tile>)
class Group(var tiles: MutableList<Tile>)

fun getTileSet(): MutableList<Tile> {
    return setOf(
        Tile.getNumberRange().map { number ->
            Tile.getColorRange().map { color ->
                Tile(color, number, 1)
            }
        }.flatten(),
        Tile.getNumberRange().map { number ->
            Tile.getColorRange().map { color ->
                Tile(color, number, 2)
            }
        }.flatten(),
        setOf(
            Tile(TileColor.BLACK, null, 1),
            Tile(TileColor.RED, null, 1)
        )
    ).flatten().toMutableList()
}


