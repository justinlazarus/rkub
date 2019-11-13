
class Game(playerNames: List<String>) {

    private var gameState = State(
        pool = Pool(getTileSet()),
        inPlay = InPlay(mutableListOf<Meld>()),
        players = Player.buildPlayers(playerNames)
    )

    fun start() {
        gameState.run { state ->
            do { players.forEach {
                it.state?. ?: it.addToGame(gameState)
                it.play(this)
            }
            } while (players.filter { it.isRackEmpty() }.count() == 0 )
        }
    }

    class State(var pool: Pool, var inPlay: InPlay, var players: List<Player>)
}

fun main() {
    Game(listOf("Justin", "Angel", "Lily")).start()
}
