import java.util.*

enum class TileColor { BLACK, BLUE, RED, ORANGE }

inline fun <reified T: Enum<T>> T.next(): T {

    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}

class Tile(val number: Int, val color: TileColor): Comparable<Tile> {
    val id = UUID.randomUUID()

    override fun compareTo(other: Tile): Int {
        return this.number.compareTo(this.number).and(this.color.compareTo(this.color))
    }

    override fun equals(other: Any?): Boolean {
        val tile = other as Tile

        return (
            // Tile number equality
            this.number.equals(this.number)
            .and(tile.number.equals(this.number))
            .and(this.number.equals(tile.number))
            and(tile.number.equals(null)).not()

            // Tile color equality
            .and(this.color.equals(this.color))
            .and(tile.color.equals(this.color))
            .and(this.color.equals(tile.color))
            .and(tile.color.equals(null).not())

            // Tile id equality
            .and(this.id.equals(this.id))
            .and(tile.id.equals(this.id))
            .and(this.id.equals(tile.id))
            .and(tile.id.equals(null).not())
        )
    }
}

class TileRange(override val start: Tile, override val endInclusive: Tile): ClosedRange<Tile>, Iterable<Tile> {
    override fun iterator(): Iterator<Tile> = TileIterator(start, endInclusive)
    override fun contains(tile: Tile): Boolean = tile in start..endInclusive
}

class TileIterator(val start: Tile, var endInclusive: Tile): MutableIterator<Tile> {
    var currentTile = start

    override fun remove(): Unit {
        this.endInclusive = Tile(endInclusive.number.dec(), endInclusive.color)
    }
    override fun hasNext(): Boolean = currentTile.number <= endInclusive.number
    override fun next(): Tile = Tile(currentTile.number.inc(), currentTile.color)
}

class TileCollection(tiles: MutableList<Tile>): MutableCollection<Tile>, Comparable<Tile> {
    override val size: Int
        get() = this.count()

    override fun add(element: Tile): Boolean {
        return this.add(element)
    }

    override fun compareTo(other: Tile): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(element: Tile): Boolean {
        return element in this
    }

    override fun containsAll(elements: Collection<Tile>): Boolean {
        return this.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return this.isEmpty()
    }

    override fun addAll(elements: Collection<Tile>): Boolean {
        return this.addAll(elements)
    }

    override fun clear() {
        return this.clear()
    }

    override fun iterator(): MutableIterator<Tile> {
        return TileIterator(this.first(), this.last())
    }

    override fun remove(element: Tile): Boolean {
        return this.remove(element)
    }

    override fun removeAll(elements: Collection<Tile>): Boolean {
        return this.removeAll(elements)
    }

    override fun retainAll(elements: Collection<Tile>): Boolean {
        return this.retainAll(elements)
    }

}
