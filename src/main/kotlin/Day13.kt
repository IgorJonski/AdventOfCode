import java.io.File
import kotlin.math.min

fun main() {
    day13task1()
}

fun day13task1() {
    val file = File("src/main/resources/day13_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines().toList()
    val grids = mutableListOf<List<List<Char>>>()
    val tempGrid = mutableListOf<List<Char>>()
    for (line in lines) {
        if (line.isBlank()) {
            grids.add(tempGrid.toList())
            tempGrid.clear()
            continue
        }
        tempGrid.add(line.toList())
    }
    grids.add(tempGrid.toList())

    var sum = 0L
    for (grid in grids) {
        val horizontalMirrorValue = locateHorizontalMirror(grid)
        val transposedGrid = transpose(grid)
        val verticalMirrorValue = locateHorizontalMirror(transposedGrid)
        sum += horizontalMirrorValue * 100 + verticalMirrorValue
    }
    println(sum)
}

fun locateHorizontalMirror(grid: List<List<Char>>): Long {
    for (index in 1 ..< grid.size) {
        val rowRange = min(index, grid.size - index)
        var isMirror = true
        for (row in 1..rowRange) {
            if (grid[index - row] != grid[index + row - 1]) {
                isMirror = false
                break
            }
        }
        if (isMirror)
            return index.toLong()
    }
    return 0L
}

fun transpose(grid: List<List<Char>>): List<List<Char>> {
    val numRows = grid.size
    val numCols = grid[0].size
    val transposedGrid = MutableList(numCols) { MutableList(numRows) { ' ' } }
    for (i in 0 ..< numRows) {
        for (j in 0 ..< numCols) {
            transposedGrid[j][i] = grid[i][j]
        }
    }
    return transposedGrid
}