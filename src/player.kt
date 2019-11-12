class Player(var name: String, var position: Position) {

    companion object {
        const val INITIAL_MELD_TARGET = 30
        const val MAX_NUMBER_OF_PLAYERS = 4

        enum class Position { TOP, RIGHT, BOTTOM, LEFT }

        fun buildPlayers(names: List<String>): List<Player> {
            return names.mapIndexed { index, name -> Player(name, Position.values()[index]) }
        }
    }

    private var rack = fillRack(Bin(mutableListOf()))
    private var playedInitialMeld = false

    private fun fillRack(pool: Bin): Bin {
        return pool.also { bin ->
            repeat(
                times = Tile.NUMBER_OF_TILES_PER_RACK,
                action = { bin.addRandomTile(pool) }
            )
        }
    }

    fun isRackEmpty(): Boolean {
      return rack.tiles.isEmpty()
    }

    fun play(gameState: Game.State) {
        Turn(gameState, this)
        .also { if (this.playedInitialMeld) it.playStandard() else it.playInitialMeld() }
    }

    private class Turn(var gameState: Game.State, var player: Player) {

        fun playInitialMeld( ): Boolean {
            val groups = getGroups(Bin(player.rack.tiles))
            return true
        }

        fun playStandard(): Boolean {
            val groups = getGroups(getStandardPlayBin())
            return true
        }

        fun getStandardPlayBin(): Bin {
            return Bin(listOf(player.rack.tiles, gameState.played.tiles).flatten().toMutableList())
        }

        private fun getRuns(): List<Run> {
            return listOf()
        }

        private fun getGroups(bin: Bin): List<Group> {
            val potentialNumbers = bin.tiles
                .sortedByDescending { it.number }
                .groupingBy { Pair(it.number, it) }.eachCount().keys
                .groupingBy { it.first }.eachCount()
                .filter { bin.getGroupFilter(it) }.keys
                .map { potential -> bin.tiles.filter { it.number == potential } }
                .flatten()

            return listOf()
        }
    }
}

