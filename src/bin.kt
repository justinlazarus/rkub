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

    fun getGroupFilter(tile: Map.Entry<Int?, Int>): Boolean {
        return when(this.hasJokerTiles()) {
            true -> (tile.value > 1)
            false -> (tile.value > 2)
        }
    }

    fun hasJokerTiles(): Boolean {
        return (this.tiles.filter { it.number == null }.count() > 0)
    }
}

