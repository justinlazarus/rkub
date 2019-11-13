import kotlin.random.Random

inline class TileId(val tileId: Int)

open class Meld(var tiles: MutableList<Tile>) {

    val tileCount: Int = this.tiles.size
    val score: Int = this.tiles.map { it.tileNumber.number }.sum()

}

open class Bin(var tiles: MutableMap<TileId, Tile>) {

    val tileCount: Int = this.tiles.size
    val jokerCount: Int = this.tiles.filter { it.value.isJoker }.size
    val numericCount: Int = this.tiles.filter { !it.value.isJoker }.size
    val score: Int = this.tiles.map { it.value.tileNumber.number }.sum()

    fun addTile(tile: Tile) {
        tiles[tile.tileNumber.id] = tile
    }

    fun removeTile(tile: Tile): Tile? = tiles.remove(tile.tileNumber.id)

    fun fill(newTiles: List<Tile>) {
        tiles = newTiles.associateBy { it.tileNumber.id }.toMutableMap()
    }

    fun empty() {
        tiles = mutableMapOf()
    }

}

class TileNumber(val number: Int): Comparable<TileNumber>{

    var id: TileId
        get() = this.id
        set(id) { this.id = id }

    override fun compareTo(other: TileNumber): Int = this.number.compareTo(other.number)
    operator fun inc(): TileNumber = TileNumber(number + 1)

}

class TileNumberRange(override val start: TileNumber, override val endInclusive: TileNumber ): ClosedRange<TileNumber>, Iterable<TileNumber> {

    override fun iterator(): Iterator<TileNumber> = NumberIterator(start, endInclusive)
    override fun contains(number: TileNumber): Boolean = number in start..endInclusive

}

class NumberIterator(val start: TileNumber, val endInclusive: TileNumber) : Iterator<TileNumber> {

    var initValue = start
    override fun hasNext(): Boolean = initValue <= endInclusive
    override fun next(): TileNumber = initValue++

}

class TileColor(val color: Int): Comparable<TileColor> {

    override fun compareTo(other: TileColor): Int = this.color.compareTo(other.color)
    operator fun inc(): TileColor = TileColor(color + 1)

}

class TileColorRange( override val start: TileColor, override val endInclusive: TileColor ): ClosedRange<TileColor>, Iterable<TileColor> {

    override fun iterator(): Iterator<TileColor> = ColorIterator(start, endInclusive)
    override fun contains(color: TileColor): Boolean = color in start..endInclusive

}

class ColorIterator(val start: TileColor, val endInclusive: TileColor) : Iterator<TileColor> {

    var initValue = start
    override fun hasNext(): Boolean = initValue <= endInclusive
    override fun next(): TileColor = initValue++

}

class Tile(var tileColor: TileColor, var tileNumber: TileNumber) {

    companion object {
        val RED = TileColor(1)
        val ORANGE = TileColor(2)
        val BLUE = TileColor(3)
        val BLACK = TileColor(4)

        val colors = TileColorRange(RED, BLACK)
        val numbers = TileNumberRange(TileNumber(1), TileNumber(13))
    }

    val isJoker: Boolean = this.tileNumber.number == 0
    val isNumeric: Boolean = this.tileNumber.number != 0
    val score: Int = this.tileNumber.number

    fun activateJoker(color: TileColor, number: TileNumber) {
        this.tileColor = color
        this.tileNumber = number
    }

}

class Run(var color: TileColor, tiles: MutableList<Tile>): Meld(tiles) {

    val max = tiles.maxBy { it.tileNumber }?.tileNumber ?: TileNumber(0)
    val min = tiles.minBy { it.tileNumber }?.tileNumber ?: TileNumber(0)
    val potentials = Tile.Companion.numbers.filter { it !in TileNumberRange(min, max) }

    fun shift(tile: Tile): Tile? {
        val tileToShift = when{
            tile.tileNumber !in min..max -> null
            tile.tileNumber < min -> tiles[max.id.tileId]
            tile.tileNumber > max -> tiles[min.id.tileId]
            else -> null
        }
        tiles.add(tile)
        return tileToShift
    }

    fun split(tile: Tile): Run? {
        var newLists = when {
            tiles.size < 6 -> Pair(null, null)
            tile.tileNumber !in min..max -> Pair(null, null)
            else -> tiles.partition { it.tileNumber < tile.tileNumber }
        }

        newLists.first?.let { tiles = it.toMutableList() }
        return newLists.second?.let{ Run(color, it.toMutableList())}
    }

}

class Group(var number: TileNumber, tiles: MutableList<Tile>): Meld(tiles) {

    val max = tiles.maxBy { it.tileColor }?.tileColor ?: TileColor(0)
    val min: TileColor = tiles.minBy { it.tileColor.color }?.tileColor ?: TileColor(0)
    val potentials = Tile.Companion.colors.filter { it !in TileColorRange(min, max) }

}

class Rack(tiles: MutableMap<TileId, Tile>): Bin(tiles) {

    val totalScore: Int = tiles.toList().sumBy { it.second.score }

    fun playTile(tile: Tile): Tile? {
        return tiles.remove(tile.tileNumber.id)
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
    val potentialRuns: Map<TileColor, List<TileNumber>> = melds.filterIsInstance<Run>().associate {
        it.color to it.potentials
    }
    val potentialGroups: Map<TileNumber, List<TileColor>> = melds.filterIsInstance<Group>().associate {
        it.number to it.potentials
    }

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

fun getTileSet(): MutableMap<TileId, Tile> {

    // Get a list of tiles used in the game. 104 numeric/colored tiles + 2 jokers. Jokers are defined with a tile
    // number of zero.
    val initialTiles =  setOf(
        Tile.numbers.map { number -> Tile.colors.map { color -> Tile(color, number) } }.flatten(),
        Tile.numbers.map { number -> Tile.colors.map { color -> Tile(color, number) } }.flatten(),
        setOf(Tile(Tile.RED, TileNumber(0)), Tile(Tile.BLACK, TileNumber(0)))
    ).flatten().toMutableList()

    initialTiles
        .also { tiles -> tiles.forEachIndexed { index, tile -> tile.tileNumber.id = TileId(index) } }
        .also { tiles -> return tiles.associateBy { it.tileNumber.id }.toMutableMap() }

}


