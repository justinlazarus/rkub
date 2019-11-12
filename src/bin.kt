import kotlin.random.Random

class Bin(var tiles: MutableList<Tile>) {

    fun removeRandomTile(): Tile {
        return this.tiles.removeAt(Random.nextInt(1, this.tiles.size))
    }

    fun addRandomTile(from: Bin) {
        this.tiles.add(from.removeRandomTile())
    }

    fun getNumericTiles(): List<Tile> {
      return this.tiles.filter { it.number != null }
    }

    fun getJokerTiles(): List<Tile> {
        return this.tiles.filter { it.number == null }
    }

    fun hasJokerTiles(): Boolean {
        return (this.tiles.filter { it.number == null }.count() > 0)
    }

    fun getGroupFilter(tileSet: Map.Entry<Int?, Int>): Boolean {
        return when(this.hasJokerTiles()) {
            true -> (tileSet.value > 2)
            false -> (tileSet.value == 2)
        }
    }
}

