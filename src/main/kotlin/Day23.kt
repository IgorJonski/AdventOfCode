import java.io.File
import java.util.Stack

fun main() {
//    day23task1()
    day23part2()
}

data class WalkNode(var point: Point, var actions : MutableList<Point> = mutableListOf())

/**
 * To work, edit ending point in input file to be 'F'.
 * Starting point is always (1, 0)
 */
fun day23task1() {
    val file = File("src/main/resources/day23_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val grid = lines.map { it.toList() }
    var longestPath = 0

    val startingPoint = Point(1, 0)
    val endingPoint = grid.last().indexOf('F').let { Point(it, grid.size - 1) }

    val stack = Stack<WalkNode>()
    stack.push(WalkNode(startingPoint, mutableListOf(startingPoint)))

    while (stack.isNotEmpty()) {
        val node = stack.pop()
        val nextPossibleWalkNodes = getAvailableMoves(node, grid, endingPoint)
        if  (nextPossibleWalkNodes.first.isEmpty() && nextPossibleWalkNodes.second) {
            println("Finished some path in ${node.actions.size - 1} steps")
            if (node.actions.size - 1 > longestPath) {
                longestPath = node.actions.size - 1
            }
            continue
        }
        for (walkNode in nextPossibleWalkNodes.first) {
            stack.push(walkNode)
        }
    }

    println("Longest path is $longestPath")
}


/**
 * To work, edit starting and ending point in input file to be 'T'.
 */
fun day23part2() {
    val file = File("src/main/resources/day23_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    var grid = lines.map { it.toList() }
    grid = changeAllSlopesToNormalGround(grid)
    val markedGrid = markIntersections(grid)
    val intersections = mutableListOf<Point>()
    for (y in markedGrid.indices) {
        for (x in markedGrid[0].indices) {
            if (markedGrid[y][x] == 'T') {
                intersections.add(Point(x, y))
            }
        }
    }

    val graphOfIntersections = hashMapOf<Point, MutableList<Pair<Point, Int>>>()
    for (intersection in intersections) {
        val stack = Stack<WalkNode>()
        val walkNode = WalkNode(intersection, mutableListOf(intersection))

        val availableNextPointFromIntersection = getAvailableMoves(walkNode, grid, Point(-1, -1))
        for (availableNode in availableNextPointFromIntersection.first) {
            stack.push(availableNode)

            while (stack.isNotEmpty()) {
                val node = stack.pop()
                val nextPossibleWalkNode = getNextMoveInCreatingConnectionsBetweenGraphElements(node, grid)

                if (nextPossibleWalkNode.point in intersections) {
                    val distance = node.actions.size
                    if (graphOfIntersections[intersection] == null) {
                        graphOfIntersections[intersection] = mutableListOf()
                    }
                    graphOfIntersections[intersection]!!.add(Pair(nextPossibleWalkNode.point, distance))
                    continue
                }

                stack.push(nextPossibleWalkNode)
            }
        }
    }

    val startingPoint = graphOfIntersections.filter { it.key == Point(1, 0) }.keys.first()
    val endingPoint = graphOfIntersections.toSortedMap(compareBy { it.y }).filter { it.key.y == grid.size - 1 }.keys.first()
    var longestPath = 0

    val stack = Stack<Pair<Point, MutableList<Point>>>()
    stack.push(Pair(startingPoint, mutableListOf(startingPoint)))
    while (stack.isNotEmpty()) {
        val node = stack.pop()
        if (node.first == endingPoint) {
            val distanceTravelled = getFullDistanceFromAPathOfNodes(node.second, graphOfIntersections)
            if (distanceTravelled > longestPath) {
                longestPath = distanceTravelled
            }
            println("Finished some path in ${node.second.size - 1} steps with distance $distanceTravelled")
        }
        for (nextNode in graphOfIntersections[node.first]!!) {
            if (node.second.contains(nextNode.first)) {
                continue
            }
            stack.push(Pair(nextNode.first, node.second.toMutableList().apply { add(nextNode.first) }))
            println("Pushed ${nextNode.first}")
        }
    }

    println("Longest path is $longestPath")
}

fun getFullDistanceFromAPathOfNodes(path: List<Point>, graphOfIntersections: HashMap<Point, MutableList<Pair<Point, Int>>>): Int {
    var distance = 0
    for (i in 0 ..< path.size - 1) {
        val firstPoint = path[i]
        val secondPoint = path[i + 1]
        distance += graphOfIntersections[firstPoint]!!.first { it.first == secondPoint }.second
    }
    return distance
}

fun getAvailableMoves(walkNode: WalkNode, grid: List<List<Char>>, endingPoint: Point): Pair<List<WalkNode>, Boolean> {
    val availableMoves = mutableListOf<WalkNode>()
    if (walkNode.point == endingPoint) {
        return Pair(availableMoves, true)
    }
    val x = walkNode.point.x
    val y = walkNode.point.y
    val slopes = listOf('>', '<', '^', 'v')

    if (grid[y][x] in slopes) {
        val slope = grid[y][x]
        when (slope) {
            '>' -> {
                if (checkIfPointIsInTheGrid(Point(x + 1, y), grid) && grid[y][x + 1] != '#' && !walkNode.actions.contains(Point(x + 1, y))) {
                    availableMoves.add(WalkNode(Point(x + 1, y), walkNode.actions.toMutableList().apply { add(Point(x + 1, y)) }))
                }
            }
            '<' -> {
                if (checkIfPointIsInTheGrid(Point(x - 1, y), grid) && grid[y][x - 1] != '#' && !walkNode.actions.contains(Point(x - 1, y))) {
                    availableMoves.add(WalkNode(Point(x - 1, y), walkNode.actions.toMutableList().apply{ add(Point(x - 1, y)) }))
                }
            }
            '^' -> {
                if (checkIfPointIsInTheGrid(Point(x, y - 1), grid) && grid[y - 1][x] != '#' && !walkNode.actions.contains(Point(x, y - 1))) {
                    availableMoves.add(WalkNode(Point(x, y - 1), walkNode.actions.toMutableList().apply { add(Point(x, y - 1)) }))
                }
            }
            'v' -> {
                if (checkIfPointIsInTheGrid(Point(x, y + 1), grid) && grid[y + 1][x] != '#' && !walkNode.actions.contains(Point(x, y + 1))) {
                    availableMoves.add(WalkNode(Point(x, y + 1), walkNode.actions.toMutableList().apply { add(Point(x, y + 1)) }))
                }
            }
        }
        return Pair(availableMoves, false)
    }

    if (checkIfPointIsInTheGrid(Point(x - 1, y), grid) && grid[y][x - 1] != '#' && !walkNode.actions.contains(Point(x - 1, y))) {
        availableMoves.add(WalkNode(Point(x - 1, y), walkNode.actions.toMutableList().apply{ add(Point(x - 1, y)) }))
    }
    if (checkIfPointIsInTheGrid(Point(x + 1, y), grid) && grid[y][x + 1] != '#' && !walkNode.actions.contains(Point(x + 1, y))) {
        availableMoves.add(WalkNode(Point(x + 1, y), walkNode.actions.toMutableList().apply { add(Point(x + 1, y)) }))
    }
    if (checkIfPointIsInTheGrid(Point(x, y - 1), grid) && grid[y - 1][x] != '#' && !walkNode.actions.contains(Point(x, y - 1))) {
        availableMoves.add(WalkNode(Point(x, y - 1), walkNode.actions.toMutableList().apply { add(Point(x, y - 1)) }))
    }
    if (checkIfPointIsInTheGrid(Point(x, y + 1), grid) && grid[y + 1][x] != '#' && !walkNode.actions.contains(Point(x, y + 1))) {
        availableMoves.add(WalkNode(Point(x, y + 1), walkNode.actions.toMutableList().apply { add(Point(x, y + 1)) }))
    }
    return Pair(availableMoves, false)
}

fun checkIfPointIsInTheGrid(point: Point, grid: List<List<Char>>): Boolean {
    return point.x >= 0 && point.x < grid[0].size && point.y >= 0 && point.y < grid.size
}

fun getNextMoveInCreatingConnectionsBetweenGraphElements(walkNode: WalkNode, grid: List<List<Char>>): WalkNode {
    val x = walkNode.point.x
    val y = walkNode.point.y

    if (checkIfPointIsInTheGrid(Point(x - 1, y), grid) && grid[y][x - 1] != '#' && !walkNode.actions.contains(Point(x - 1, y))) {
        return WalkNode(Point(x - 1, y), walkNode.actions.toMutableList().apply{ add(Point(x - 1, y)) })
    }
    if (checkIfPointIsInTheGrid(Point(x + 1, y), grid) && grid[y][x + 1] != '#' && !walkNode.actions.contains(Point(x + 1, y))) {
        return WalkNode(Point(x + 1, y), walkNode.actions.toMutableList().apply { add(Point(x + 1, y)) })
    }
    if (checkIfPointIsInTheGrid(Point(x, y - 1), grid) && grid[y - 1][x] != '#' && !walkNode.actions.contains(Point(x, y - 1))) {
        return WalkNode(Point(x, y - 1), walkNode.actions.toMutableList().apply { add(Point(x, y - 1)) })
    }
    if (checkIfPointIsInTheGrid(Point(x, y + 1), grid) && grid[y + 1][x] != '#' && !walkNode.actions.contains(Point(x, y + 1))) {
        return WalkNode(Point(x, y + 1), walkNode.actions.toMutableList().apply { add(Point(x, y + 1)) })
    }

    throw Exception("No next move")
}

fun changeAllSlopesToNormalGround(grid: List<List<Char>>): List<List<Char>> {
    val resultGrid = grid.map { it.toMutableList() }
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] in listOf('>', '<', '^', 'v')) {
                resultGrid[y][x] = '.'
            }
        }
    }
    return resultGrid
}

fun markIntersections(grid: List<List<Char>>): List<List<Char>> {
    val height = grid.size
    val width = grid[0].size
    val resultGrid = grid.map { it.toMutableList() }

    for (y in 1 ..< height - 1) {
        for (x in 1 ..< width - 1) {
            if (grid[y][x] != '#') {
                val up = grid[y - 1][x]
                val down = grid[y + 1][x]
                val left = grid[y][x - 1]
                val right = grid[y][x + 1]

                val isTurn =
                        (up == '.' && down == '.' && left == '.' && right == '#') ||
                        (up == '.' && down == '.' && left == '#' && right == '.') ||
                        (up == '.' && down == '#' && left == '.' && right == '.') ||
                        (up == '#' && down == '.' && left == '.' && right == '.') ||
                        (up == '.' && down == '.' && left == '.' && right == '.')

                if (isTurn) {
                    resultGrid[y][x] = 'T'
                }
            }
        }
    }

    return resultGrid
}