class TileNumber(var number: Int): Comparable<TileNumber>{

    override fun compareTo(other: TileNumber): Int = this.number.compareTo(other.number)
    operator fun inc(): TileNumber = TileNumber(number + 1)

}

class TileNumberRange(
    override val start: TileNumber, override val endInclusive: TileNumber
): ClosedRange<TileNumber>, Iterable<TileNumber> {

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

class TileColorRange(
    override val start: TileColor, override val endInclusive: TileColor
): ClosedRange<TileColor>, Iterable<TileColor> {

    override fun iterator(): Iterator<TileColor> = ColorIterator(start, endInclusive)
    override fun contains(color: TileColor): Boolean = color in start..endInclusive

}

class ColorIterator(val start: TileColor, val endInclusive: TileColor) : Iterator<TileColor> {

    var initValue = start
    override fun hasNext(): Boolean = initValue <= endInclusive
    override fun next(): TileColor = initValue++

}

