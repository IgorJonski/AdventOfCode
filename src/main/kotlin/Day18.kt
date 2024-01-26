import java.io.File
import java.nio.charset.CoderResult

fun main() {
//    day18task1()
    day18task2()
}

data class Trench(var x: Int, var y: Int)
data class Corner(var x: Long, var y: Long)

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

fun day18task2() {
    val file = File("src/main/resources/day18_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val outsideTrenchesCorners = mutableSetOf<Corner>()
    outsideTrenchesCorners.add(Corner(0L, 0L))
    var pointsOnEdges = 0L
    val lines = file.readLines()
    for (line in lines) {
        val hexColor = line.substringAfter("#").substringBefore(")")
        val (direction, distance) = decodeDirectionAndDistanceFromHexColorValue(hexColor)
        val lastCorner = outsideTrenchesCorners.last()
        outsideTrenchesCorners.add(
            when (direction) {
                "U" -> Corner(lastCorner.x, lastCorner.y - distance)
                "D" -> Corner(lastCorner.x, lastCorner.y + distance)
                "R" -> Corner(lastCorner.x + distance, lastCorner.y)
                "L" -> Corner(lastCorner.x - distance, lastCorner.y)
                else -> throw Exception("Unknown direction")
            }
        )
        pointsOnEdges += distance
    }

    var interiorPoints = calculateNumberOfInnerPoints(outsideTrenchesCorners.toList())
    interiorPoints += pointsOnEdges
    interiorPoints /= 2
    interiorPoints += 1
    println(interiorPoints)
}


// https://www.mathopenref.com/coordpolygonarea.html
// this result doesn't include the points on the edges
// after adding them, the result needs to be divided by 2
fun calculateNumberOfInnerPoints(corners: List<Corner>): Long {
    var innerPoints = 0L

    for (i in 0..<corners.size - 1) {
        val currentCorner = corners[i]
        val nextCorner = corners[i + 1]
        innerPoints += currentCorner.x * nextCorner.y - nextCorner.x * currentCorner.y
    }

    return innerPoints
}

fun decodeDirectionAndDistanceFromHexColorValue(hexColor: String): Pair<String, Long> {
    val direction = hexColor.last()
    val distance = hexColor.substring(0, hexColor.length - 1).toLong(16)

    val decodedDirection = when (direction) {
        '0' -> "R"
        '1' -> "D"
        '2' -> "L"
        '3' -> "U"
        else -> throw Exception("Unknown direction")
    }

    return Pair(decodedDirection, distance)
}