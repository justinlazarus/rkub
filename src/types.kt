import java.util.*

class Tile(val number: TileNumber, val color: TileColor, val id: Int): Comparable<Tile> {
    override fun compareTo(other: Tile): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class TileNumber(var number: Int): Comparable<TileNumber>{
    override fun compareTo(other: TileNumber): Int = this.number.compareTo(other.number)
    operator fun inc(): TileNumber = TileNumber(number + 1)
}

class TileColor(val color: Int): Comparable<TileColor> {
    override fun compareTo(other: TileColor): Int = this.color.compareTo(other.color)
    operator fun inc(): TileColor = TileColor(color + 1)
}

class TileNumberRange( override val start: TileNumber, override val endInclusive: TileNumber ): ClosedRange<TileNumber>, Iterable<TileNumber> {
    override fun iterator(): Iterator<TileNumber> = TileNumberIterator(start, endInclusive)
    override fun contains(number: TileNumber): Boolean = number in start..endInclusive
}

class TileColorRange( override val start: TileColor, override val endInclusive: TileColor ): ClosedRange<TileColor>, Iterable<TileColor> {
    override fun iterator(): Iterator<TileColor> = TileColorIterator(start, endInclusive)
    override fun contains(color: TileColor): Boolean = color in start..endInclusive
}

class TileNumberIterator(val start: TileNumber, val endInclusive: TileNumber) : Iterator<TileNumber> {
    var initValue = start
    override fun hasNext(): Boolean = initValue <= endInclusive
    override fun next(): TileNumber = initValue++
}

class TileColorIterator(val start: TileColor, val endInclusive: TileColor) : Iterator<TileColor> {
    var initValue = start
    override fun hasNext(): Boolean = initValue <= endInclusive
    override fun next(): TileColor = initValue++
}

class TileIterator(val start: Tile, val endInclusive: Tile): Iterator<Tile> {
    var initValue = start
    override fun hasNext(): Boolean = initValue.id <= endInclusive.id
    override fun next(): Tile = initValue++
}

class TileCollection(tiles: MutableList<Tile>): MutableCollection<Tile>, Comparable<Tile> {
        override fun add(element: Tile): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun compareTo(other: Tile): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun contains(element: Tile): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun containsAll(elements: Collection<Tile>): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isEmpty(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun addAll(elements: Collection<Tile>): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun clear() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun iterator(): MutableIterator<Tile> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun remove(element: Tile): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun removeAll(elements: Collection<Tile>): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun retainAll(elements: Collection<Tile>): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override val size: Int
        get() = this.count()
    }
}