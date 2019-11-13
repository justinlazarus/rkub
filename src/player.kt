class Player(val name: String, val position: Position, val game: Game) {

    companion object {

        const val INITIAL_MELD_TARGET = 30
        const val MAX_NUMBER_OF_PLAYERS = 4
        const val INITIAL_RACK_COUNT = 14

        enum class Position { TOP, RIGHT, BOTTOM, LEFT }

        fun addToGame(name: String, position: Int, game: Game) = Player(
            name, Player.Companion.Position.values()[position], game
        ).also { it.fillRack() }

    }

    private var rack = Rack(mutableMapOf())
    private var playedInitialMeld = false

    fun isWinner(): Boolean = rack.tiles.isEmpty()

    fun takeTurn() {
        when(playedInitialMeld) {
            true -> this.playStandard()
            false -> this.playInitialMeld()
        }
    }

    private fun fillRack() = repeat(INITIAL_RACK_COUNT) { rack.addTile(game.pool.pullRandomTile()) }

    private fun playInitialMeld( ): Boolean {
        rack.getPotentialRuns()
        return true
    }

    private fun playStandard(): Boolean {
        return true
    }
/*
    private fun getStandardPlayBin(): Bin {
        return Bin(listOf(player.rack.tiles, gameState.played.tiles).flatten().toMutableList())
    }

    private fun getRuns(): List<Run> {
        return listOf()
    }

    private fun getGroups(bin: Bin): List<Group> {
        val tiles = bin.tiles
            .sortedByDescending { it.number }
            .groupingBy { Pair(it.number, it) }.eachCount().keys
            .groupingBy { it.first }.eachCount()
            .filter { bin.getGroupFilter(it) }.keys
            .map { potential -> bin.tiles.filter { it.number == potential } }
            .flatten()

        // First take any tiles that can make a group without jokers
        val jokerlessGroups = tiles
            .groupingBy { it.number }.eachCount().filter { it.value > 2 }.keys
            .map { potential -> bin.tiles.filter { it.number == potential } }.flatten()
            .groupBy { it.number }.map { Group(it.value) }

        return listOf()
    }
    */
}

