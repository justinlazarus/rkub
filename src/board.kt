import kotlin.properties.Delegates
import kotlin.random.Random

open class Meld(var tiles: MutableList<Tile>) {

    val tileCount: Int = this.tiles.size
    val score: Int = this.tiles.map { it.number.number }.sum()

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
        const val TOTAL_TILE_COUNT = 106
        val RED = TileColor(1)
        val ORANGE = TileColor(2)
        val BLUE = TileColor(3)
        val BLACK = TileColor(4)

        val tileIds = 1..TOTAL_TILE_COUNT
        val colors = TileColorRange(RED, BLACK)
        val numbers = TileNumberRange(TileNumber(1), TileNumber(13))

        fun getTileset(): MutableMap<Int, Tile> {
            return with(tileIds.iterator()) {
                listOf(getTiles(this), getTiles(this), getJokers(this))
                    .flatten().associateBy { it.id }.toMutableMap()
            }
        }

        private fun getTiles(idGenerator: IntIterator) = Tile.numbers .map { number -> Tile.colors
            .map { color -> Tile(color, number, idGenerator.next()) }
        }.flatten()

        private fun getJokers(idGenerator: IntIterator) = setOf(
            Tile(Tile.RED, TileNumber(0), idGenerator.next()),
            Tile(Tile.BLACK, TileNumber(0), idGenerator.next())
        )
    }

    val isJoker: Boolean = this.number.number == 0
    val isNumeric: Boolean = this.number.number != 0
    val score: Int = this.number.number

    fun activateJoker(color: TileColor, number: TileNumber) {
        this.color = color
        this.number = number
    }

}

class Run(var color: TileColor, tiles: MutableList<Tile>): Meld(tiles) {

    val max = tiles.maxBy { it.number }?.number ?: TileNumber(-1)
    val min = tiles.minBy { it.number }?.number ?: TileNumber(-1)
    val potentials = Tile.Companion.numbers.filter { it !in TileNumberRange(min, max) }

    fun shift(tile: Tile): Tile? {
        val tileToShift = when{
            tile.number !in min..max -> null
            tile.number < min -> tiles.
            tile.number > max -> tiles[min.id]
            else -> null
        }
        tiles.add(tile)
        return tileToShift
    }

    fun split(tile: Tile): Run? {
        var newLists = when {
            tiles.size < 6 -> Pair(null, null)
            tile.number !in min..max -> Pair(null, null)
            else -> tiles.partition { it.number < tile.number }
        }

        newLists.first?.let { tiles = it.toMutableList() }
        return newLists.second?.let{ Run(color, it.toMutableList())}
    }

}

class Group(var number: TileNumber, tiles: MutableList<Tile>): Meld(tiles) {

    val max = tiles.maxBy { it.color }?.color ?: TileColor(0)
    val min: TileColor = tiles.minBy { it.color.color }?.color ?: TileColor(0)
    val potentials = Tile.Companion.colors.filter { it !in TileColorRange(min, max) }

}

class Rack(tiles: MutableMap<TileId, Tile>): Bin(tiles) {

    val totalScore: Int = tiles.toList().sumBy { it.second.score }

    fun playTile(tile: Tile): Tile? {
        return tiles.remove(tile.number.id)
    }

    fun getPotentialRuns(): Map<TileColor, List<TileNumber>> {
        val countsPerColor = tiles.values
            .sortedByDescending { it.number }
            .groupingBy { Pair(it.color, it) }.eachCount().keys

        val potentialColors = countsPerColor.groupingBy { it.first }
        return mapOf()
    }

}

class Pool(tiles: MutableMap<TileId, Tile>): Bin(tiles) {

    fun pullRandomTile(): Tile {
        return this.tiles.remove(
            this.tiles.entries.elementAt(Random.nextInt(tiles.size)).key
        ) ?: throw IllegalStateException("The pool is empty!")
    }

}

class InPlay(var melds: MutableList<Meld>) {

    val meldCount: Int = melds.size
    val totalScore: Int = melds.sumBy { it.score }
    fun getPotentialRuns(): Map<TileColor, List<TileNumber>> = melds
        .filterIsInstance<Run>().associate { it.color to it.potentials }
    fun getPotentialGroups(): Map<TileNumber, List<TileColor>> = melds
        .filterIsInstance<Group>().associate { it.number to it.potentials }

    fun play(meld: Meld): Boolean {
        return when(meld) {
            is Run -> {
                when {
                    meld.tileCount < 3 -> false
                    meld.max > Tile.numbers.endInclusive -> false
                    meld.min < Tile.numbers.start -> false
                    else -> melds.add(meld)
                }
            }
            is Group -> {
                when {
                    meld.tileCount < 3 -> false
                    meld.max > Tile.colors.endInclusive -> false
                    meld.min < Tile.colors.start -> false
                    else -> melds.add(meld)
                }
            }
            else -> false
        }
    }

}

