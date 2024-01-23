import java.io.File

fun main() {
    day14task1()
}

fun day14task1() {
    val file = File("src/main/resources/day14_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val grid = mutableListOf<List<Char>>()
    for (line in lines) {
        grid.add(line.toList())
    }

    val newGrid = rollAllRoundedRocksNorth(grid)
    println(countCausedLoad(newGrid))
}

fun rollAllRoundedRocksNorth(grid: List<List<Char>>): List<List<Char>> {
    val numberOfRows = grid.size
    val numberOfColumns = grid[0].size
    val newGrid = MutableList(numberOfRows) { MutableList(numberOfColumns) { ' ' } }
    for (i in 0..<numberOfRows) {
        newGrid[i] = grid[i].toMutableList()
    }

    for (x in 0..<numberOfColumns) {
        var cubeShapedRockHeight = 0
        for (y in 0..<numberOfRows) {
            if (grid[y][x] == '#') {
                cubeShapedRockHeight = y + 1
            }
            else if (grid[y][x] == 'O') {
                newGrid[y][x] = '.'
                newGrid[cubeShapedRockHeight][x] = 'O'
                cubeShapedRockHeight++
            }
        }
    }
    return newGrid
}

fun countCausedLoad(grid: List<List<Char>>): Long {
    var numCols = grid[0].size
    var sum = 0L
    for (row in grid) {
        val numOfRoundedRocks = row.count { it == 'O' }
        sum += numOfRoundedRocks * numCols
        numCols--
    }
    return sum
}