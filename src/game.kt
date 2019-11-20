interface Strategy {


}

class Game(val playerNames: List<String>, val numericTileSetCount: Int = Game.DEFAULT_NUMERIC_SET_COUNT) {
    companion object {
        val DEFAULT_NUMERIC_SET_COUNT = 2
        val NUMERIC_TILE_COUNT = 13
        val JOKER_TILE_NUMBER = 0
    }

    val tileset: List<Tile> =
        generateSequence(1) { it + 1 }.take(numericTileSetCount)

            // Add the numeric tiles
            .map { generateSequence(TileColor.BLACK) { it.next() }.take(TileColor.values().size).map {
                    color -> getNumericTiles(color) }
            }.flatten().flatten().toMutableList()

            // Add the jokers
            .also {  it.addAll(listOf(
                Tile(Game.JOKER_TILE_NUMBER, TileColor.RED),
                Tile(Game.JOKER_TILE_NUMBER, TileColor.BLACK)
            ) ) }

    fun getNumericTiles(color: TileColor): Sequence<Tile> =
        generateSequence(1) { it + 1 }.take(Game.NUMERIC_TILE_COUNT)
        .run { this.map { number -> Tile(number, color) } }
}

fun main() {
  Game(listOf("Justin", "Mush", "Dan", "Sri"))
  return Unit
}

