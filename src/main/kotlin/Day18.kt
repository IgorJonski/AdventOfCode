import java.io.File

fun main() {
    day18task1()
}

data class Trench(var x: Int, var y: Int)

fun day18task1() {
    val file = File("src/main/resources/day18_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val outsideTrenches = mutableSetOf<Trench>()
    outsideTrenches.add(Trench(0, 0))
    val lines = file.readLines()
    for (line in lines) {
        val (direction, distance, color) = line.split(" ")
        repeat(distance.toInt()) {
            val lastTrench = outsideTrenches.last()
            outsideTrenches.add(
                when (direction) {
                    "U" -> Trench(lastTrench.x, lastTrench.y - 1)
                    "D" -> Trench(lastTrench.x, lastTrench.y + 1)
                    "R" -> Trench(lastTrench.x + 1, lastTrench.y)
                    "L" -> Trench(lastTrench.x - 1, lastTrench.y)
                    else -> throw Exception("Unknown direction")
                }
            )
        }
    }

    val interiorTrenches = generateInnerPoints(outsideTrenches.toList())
    val allTrenches = outsideTrenches + interiorTrenches
    println(outsideTrenches.size)
    println(interiorTrenches.size)
    println(allTrenches.size)
}

// https://www.youtube.com/watch?v=l_UlG-oVxus - explanation in Python
fun checkIfPointIsInsidePolygonUsingRayCasting(pointToCheck: Trench, trenches: List<Trench>): Boolean {
    val x = pointToCheck.x
    val y = pointToCheck.y
    var i = 0
    var j = trenches.size - 1
    var intersections = 0
    while (i < trenches.size) {
        val xi = trenches[i].x
        val yi = trenches[i].y
        val xj = trenches[j].x
        val yj = trenches[j].y

        val isPointBetweenYs = (yi > y) != (yj > y)
        if (isPointBetweenYs) {
            val isPointBetweenXs = (x < (xj - xi) * (y - yi) / (yj - yi) + xi)
            if (isPointBetweenXs) {
                intersections++
            }
        }
        j = i++
    }
    return intersections % 2 == 1
}

fun generateInnerPoints(trenches: List<Trench>): Set<Trench> {
    val innerPoints = mutableSetOf<Trench>()
    val minX = trenches.minOf { it.x }
    val maxX = trenches.maxOf { it.x }
    val minY = trenches.minOf { it.y }
    val maxY = trenches.maxOf { it.y }

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val point = Trench(x, y)
            if (checkIfPointIsInsidePolygonUsingRayCasting(point, trenches)) {
                innerPoints.add(point)
            }
        }
    }
    return innerPoints
}