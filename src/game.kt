import java.util.*

interface Strategy {


}



class Game(val playerNames: List<String>) {


    companion object {
        const val TOTAL_TILE_COUNT = 106

        fun getTileset(): MutableMap<Int, Tile> {
            return with(Tile.tileIds.iterator()) {
                listOf(getTiles(this), getTiles(this), getJokers(this))
                    .flatten().associateBy { it.id }.toMutableMap()
            }
        }

        private fun getTiles(idGenerator: IntIterator) = Tile.numbers.map { number -> Tile.colors
            .map { color -> Tile(color, number, idGenerator.next()) }
        }.flatten()

        private fun getJokers(idGenerator: IntIterator) = setOf(
            Tile(Tile.RED, TileNumber(30), idGenerator.next()),
            Tile(Tile.BLACK, TileNumber(30), idGenerator.next())
        )
    }

    lateinit var pool: Pool
    lateinit var inPlay: InPlay
    lateinit var players: List<Player>

    fun start() {
        with(this) {
            pool = Pool(getTileset())
            inPlay = InPlay(mutableListOf<Meld>())
            players = playerNames.mapIndexed { index, name -> Player.addToGame(name, index, this) }
        }.run { while (noWinner()) { players.forEach { it.takeTurn() } } }
    }

    private fun noWinner() = players.none { it.isWinner() }
}

data class Rules(val )
fun main() {
    Game(listOf("Justin", "Angel", "Lily")).start()
}
