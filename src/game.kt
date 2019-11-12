
class Game(playerNames: List<String>) {

    private var gameState = State(
        pool = Bin(getTileSet()),
        played = Bin(mutableListOf()),
        players = Player.buildPlayers(playerNames)
    )

    fun start() {
        gameState.run {
            do { players.forEach { it.play(this) }
            } while (players.filter { it.isRackEmpty() }.count() == 0 )
        }
    }

    class State(var pool: Bin, var played: Bin, var players: List<Player>)
}

fun main() {
    Game(listOf("Justin", "Angel", "Lily")).start()
}



