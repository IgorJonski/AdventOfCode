import java.io.File

fun main() {
    day10task1()
}

data class NodePoint(var x: Int, var y: Int)
data class Node(var hasBeenVisited: Boolean = false, var character: Char, var point: NodePoint)
enum class Direction {
    LEFT, RIGHT, TOP, BOTTOM
}

fun findNextPoint(walker: Node, nodes: List<Node>): Node? {
    val leftNodeToWalker = nodes.firstOrNull { it.point.x == walker.point.x - 1 && it.point.y == walker.point.y }
    val rightNodeToWalker = nodes.firstOrNull { it.point.x == walker.point.x + 1 && it.point.y == walker.point.y }
    val topNodeToWalker = nodes.firstOrNull { it.point.x == walker.point.x && it.point.y == walker.point.y - 1 }
    val bottomNodeToWalker = nodes.firstOrNull { it.point.x == walker.point.x && it.point.y == walker.point.y + 1}

    val availableMoves = mutableListOf<Node>()

    val legalLeftMoves = when (walker.character) {
        '-' -> listOf('-', 'L', 'F')
        '|' -> listOf()
        'F' -> listOf()
        '7' -> listOf('-', 'L', 'F')
        'J' -> listOf('-', 'L', 'F')
        'L' -> listOf()
        else -> listOf('-', 'L', 'F')
    }
    val legalRightMoves = when (walker.character) {
        '-' -> listOf('-', 'J', '7')
        '|' -> listOf()
        'F' -> listOf('-', 'J', '7')
        '7' -> listOf()
        'J' -> listOf()
        'L' -> listOf('-', 'J', '7')
        else -> listOf('-', 'J', '7')
    }
    val legalTopMoves = when (walker.character) {
        '-' -> listOf()
        '|' -> listOf('|', 'F', '7')
        'F' -> listOf()
        '7' -> listOf()
        'J' -> listOf('|', 'F', '7')
        'L' -> listOf('|', 'F', '7')
        else -> listOf('|', 'F', '7')
    }
    val legalBottomMoves = when (walker.character) {
        '-' -> listOf()
        '|' -> listOf('|', 'J', 'L')
        'F' -> listOf('|', 'J', 'L')
        '7' -> listOf('|', 'J', 'L')
        'J' -> listOf()
        'L' -> listOf()
        else -> listOf('|', 'J', 'L')
    }

    if (leftNodeToWalker != null && !leftNodeToWalker.hasBeenVisited && legalLeftMoves.contains(leftNodeToWalker.character)) {
        availableMoves.add(leftNodeToWalker)
    }
    if (rightNodeToWalker != null && !rightNodeToWalker.hasBeenVisited && legalRightMoves.contains(rightNodeToWalker.character)) {
        availableMoves.add(rightNodeToWalker)
    }
    if (topNodeToWalker != null && !topNodeToWalker.hasBeenVisited && legalTopMoves.contains(topNodeToWalker.character)) {
        availableMoves.add(topNodeToWalker)
    }
    if (bottomNodeToWalker != null && !bottomNodeToWalker.hasBeenVisited && legalBottomMoves.contains(bottomNodeToWalker.character)) {
        availableMoves.add(bottomNodeToWalker)
    }

    if (availableMoves.isEmpty()) {
        return null
    }
    if (availableMoves.size > 1) {
        println("More than one available move")
    }
    val nextPoint = availableMoves.first()
    nextPoint.hasBeenVisited = true
    return nextPoint
}

fun day10task1() {
    val file = File("src/main/resources/day10_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val grid = mutableListOf<List<Char>>()
    for (line in lines) {
        grid.add(line.toList())
    }

    val nodes = mutableListOf<Node>()
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (grid[i][j] != '.') {
                nodes.add(Node(character = grid[i][j], point = NodePoint(j, i)))
            }
        }
    }

    val startingPoint = nodes.first { it.character == 'S' }
    var walker = startingPoint
    var steps = 0
    while (true) {
        val nextPoint = findNextPoint(walker, nodes)
        if (nextPoint == null) {
            println("Path found in ${steps + 1} steps")
            break
        }
        walker = nextPoint
        steps++
    }
    println("Farthest point is ${(steps + 1) / 2} steps away")
}

fun day10task2() {
    val file = File("src/main/resources/day10_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val grid = mutableListOf<List<Char>>()
    for (line in lines) {
        grid.add(line.toList())
    }

    val nodes = mutableListOf<Node>()
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (grid[i][j] != '.') {
                nodes.add(Node(character = grid[i][j], point = NodePoint(j, i)))
            }
        }
    }

    val startingPoint = nodes.first { it.character == 'S' }
    var walker = startingPoint
    var steps = 0
    while (true) {
        val nextPoint = findNextPoint(walker, nodes)
        if (nextPoint == null) {
            println("Path found in ${steps + 1} steps")
            break
        }
        walker = nextPoint
        steps++
    }
}