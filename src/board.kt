import kotlin.random.Random

open class Meld(var tiles: MutableList<Tile>, var meldInspector: MeldInspector) {

    interface MeldInspector {
        val tiles: MutableList<Tile>
        fun getFirstTileInMeld(): Tile
        fun getLastTileInMeld(): Tile
        fun getMeldRange(): Any
    }

    val tileCount: Int = this.tiles.size
    val score: Int = this.tiles.map { it.number.number }.sum()
    val lastTileInMeld = meldInspector.getLastTileInMeld()
    val firstTileInMeld = meldInspector.getFirstTileInMeld()
    val meldRange = meldInspector.getMeldRange()
}

open class Bin(var tiles: MutableMap<Int, Tile>) {

    val tileCount: Int = this.tiles.size
    val jokerCount: Int = this.tiles.filter { it.value.isJoker }.size
    val numericCount: Int = this.tiles.filter { !it.value.isJoker }.size
    val score: Int = this.tiles.map { it.value.number.number }.sum()

    fun addTile(tile: Tile) {
        tiles[tile.id] = tile
    }

    fun removeTile(tile: Tile): Tile? = tiles.remove(tile.id)

    fun fill(newTiles: List<Tile>) {
        tiles = newTiles.associateBy { it.id }.toMutableMap()
    }

    fun empty() {
        tiles = mutableMapOf()
    }

}

class Tile(var color: TileColor, var number: TileNumber, var id: Int) {

    companion object {
        const val JOKER_SCORE = 30
        val RED = TileColor(1)
        val ORANGE = TileColor(2)
        val BLUE = TileColor(3)
        val BLACK = TileColor(4)

        val tileIds = 1..Game.TOTAL_TILE_COUNT
        val colors = TileColorRange(RED, BLACK)
        val numbers = TileNumberRange(TileNumber(1), TileNumber(13))

        fun getNullTile() = Tile(Tile.BLACK, TileNumber(-1), 0)
    }

    val isJoker: Boolean = this.number.number == JOKER_SCORE
    val isNumeric: Boolean = this.number in Tile.numbers
    val score: Int = this.number.number

    fun activateJoker(color: TileColor, number: TileNumber) {
        this.color = color
        this.number = number
    }

}

class Run(var color: TileColor, tiles: MutableList<Tile>, meldInspector: RunInspector): Meld(tiles, meldInspector) {

    class RunInspector(tiles: MutableList<Tile>): MeldInspector {
        override val tiles = tiles
        override fun getFirstTileInMeld() = tiles.minBy { it.number } ?: throw IllegalStateException("Invalid run")
        override fun getLastTileInMeld() = tiles.maxBy { it.number } ?: throw IllegalStateException("Invalid run")
        override fun getMeldRange() = TileNumberRange( getFirstTileInMeld().number, getLastTileInMeld().number )
    }

    val runRange = meldRange as TileNumberRange
    val potentials = Tile.numbers.filter { it !in runRange }

    fun shift(tile: Tile) = when {
        tile.number !in runRange -> null
        tile.number < firstTileInMeld.number -> tiles[lastTileInMeld.id]
        tile.number > lastTileInMeld.number -> tiles[firstTileInMeld.id]
        else -> null
    }.also { tiles.add(tile) }

    fun split(newTile: Tile) = when {
        tiles.size < 6 -> null
        newTile.number !in runRange -> null
        else -> tiles.partition { it.number < newTile.number }
    }.also {
        tiles = it?.first?.toMutableList() ?: throw IllegalStateException("Failed to split run")
    }.let {
        Run( color, it?.second?.toMutableList() ?: throw IllegalStateException("Failed to split run"), RunInspector(tiles) )
    }

}

class Group( var number: TileNumber, tiles: MutableList<Tile>, meldInspector: GroupInspector ): Meld(tiles, meldInspector) {

    class GroupInspector(tiles: MutableList<Tile>): MeldInspector {
        override val tiles = tiles
        override fun getFirstTileInMeld() = tiles.minBy { it.color } ?: throw IllegalStateException("Invalid group")
        override fun getLastTileInMeld() = tiles.maxBy { it.color } ?: throw IllegalStateException("Invalid group")
        override fun getMeldRange() = TileColorRange( getFirstTileInMeld().color, getLastTileInMeld().color )
    }

    val groupRange = meldRange as TileColorRange
    val potentials = Tile.colors.filter { it !in groupRange }

}

class Rack(tiles: MutableMap<Int, Tile>): Bin(tiles) {

    val totalScore: Int = tiles.toList().sumBy { it.second.score }

    fun playTile(tile: Tile): Tile? {
        return tiles.remove(tile.id)
    }

    fun getPotentialRuns(): Map<TileColor, List<TileNumber>> {
        val countsPerColor = tiles.values
            .sortedByDescending { it.number }
            .groupingBy { Pair(it.color, it) }.eachCount().keys

        val potentialColors = countsPerColor.groupingBy { it.first }
        return mapOf()
    }

}

class Pool(tiles: MutableMap<Int, Tile>): Bin(tiles) {

    fun pullRandomTile(): Tile {
        return this.tiles.remove(
            this.tiles.entries.elementAt(Random.nextInt(tiles.size)).key
        ) ?: throw IllegalStateException("The pool is empty!")
    }

}

class InPlay(var melds: MutableList<Meld>) {

    val meldCount: Int = melds.size
    val totalScore: Int = melds.sumBy { it.score }

    fun play(meld: Meld) = melds.add(meld)

}

