import java.io.File

fun main() {
//    day14task1()
    day14task2()
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

fun rollAllRoundedRocksNorth(grid: List<List<Char>>): MutableList<MutableList<Char>> {
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

fun day14task2() {
    val file = File("src/main/resources/day14_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    var grid = mutableListOf<MutableList<Char>>()
    for (line in lines) {
        grid.add(line.toMutableList())
    }
    val alreadySeenGrids = hashMapOf<List<List<Char>>, Long>()
    var newGrid = grid.map { it.toMutableList() }.toMutableList()

    repeat(1000000000) {
        newGrid = performOneFullCycle(newGrid)

        if (alreadySeenGrids.containsKey(newGrid)) {
            val alreadySeenGridValue = alreadySeenGrids[newGrid]!!
            val cycleLength = it - alreadySeenGridValue
            val numberOfComputationsWithoutFirstCycle = 1000000000 - 1 - alreadySeenGridValue
            val gridThatWillBeSeenAtTheEnd = getGridFromCycle(alreadySeenGrids, numberOfComputationsWithoutFirstCycle % cycleLength + alreadySeenGridValue)
            return println(countCausedLoad(gridThatWillBeSeenAtTheEnd))
        }

        alreadySeenGrids[newGrid] = it.toLong()
    }


    println(countCausedLoad(grid))
}

fun getGridFromCycle(hashMap: HashMap<List<List<Char>>, Long>, cycle: Long): List<List<Char>> {
    return hashMap.filter { it.value == cycle }.keys.first()
}

fun rotateGridBy90Degrees(grid: List<List<Char>>): MutableList<MutableList<Char>> {
    val rows = grid.size
    val cols = if (rows > 0) grid[0].size else 0
    val rotatedGrid = MutableList(cols) { MutableList(rows) { '.' } }

    for (i in 0 ..< rows) {
        for (j in 0 ..< cols) {
            rotatedGrid[j][rows - 1 - i] = grid[i][j]
        }
    }
    return rotatedGrid
}

fun performOneFullCycle(grid: List<List<Char>>): MutableList<MutableList<Char>> {
    var newGrid = grid.map { it.toMutableList() }.toMutableList()
    repeat(4) {
        newGrid = rollAllRoundedRocksNorth(newGrid)
        newGrid = rotateGridBy90Degrees(newGrid)
    }
    return newGrid
}