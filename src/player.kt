class Player(val name: String, val position: Position, val game: Game) {

    companion object {

        const val INITIAL_MELD_TARGET = 30
        const val MAX_NUMBER_OF_PLAYERS = 4
        const val INITIAL_RACK_COUNT = 14

        enum class Position { TOP, RIGHT, BOTTOM, LEFT }

    }
}
