
class Game(val playerNames: List<String>) {

    lateinit var pool: Pool
    lateinit var inPlay: InPlay
    lateinit var players: List<Player>

    fun start() {
        with(this) {
            pool = Pool(Tile.getTileset())
            inPlay = InPlay(mutableListOf<Meld>())
            players = playerNames.mapIndexed { index, name -> Player.addToGame(name, index, this) }
        }.run { while (noWinner()) { players.forEach { it.takeTurn() } } }
    }

    private fun noWinner() = players.none { it.isWinner() }
}

fun main() {
    Game(listOf("Justin", "Angel", "Lily")).start()
}
