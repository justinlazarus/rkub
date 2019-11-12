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

inline class Pool(val tiles: MutableList<Tile>) {
    val remaining: Int get() = tiles.count()
    val takeOne: Tile? get() = tiles.removeAt(Random.nextInt(tiles.size))

    fun getOne(): Tile? {
        return try {
            tiles.removeAt(Random.nextInt(1, tiles.size))
        } catch (e: IllegalArgumentException) { null }
    }
}

inline class GroupsInPlay(val groups: List<Group>) {

}

inline class RunsInPlay(val runs: List<Run>) {

}

inline class Run( val tiles: List<Tile>) {

    // TODO
    fun shift(): Run {
        return Run(tiles)
    }

    // TODO
    fun split(): List<Run> {
        return listOf(Run(tiles), Run(tiles))
    }
}

inline class Group(val tiles: List<Tile>) {

    // TODO
    fun getNumber(): Int {
       return 0
    }

    // TODO
    fun getMissingColor():
}

inline class Rack(val tiles: List<Tile>) {

}
