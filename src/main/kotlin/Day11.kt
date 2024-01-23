import java.io.File
import kotlin.math.abs

fun main() {
//    day11task1()
    day11task2()
}

data class Point(var x: Int, var y: Int)

fun getDistanceBetweenTwoPoints(point1: Point, point2: Point): Int {
    return abs(point1.x - point2.x) + abs(point1.y - point2.y)
}

fun findAllGalaxiesInUniverse(universe: List<List<Char>>): List<Point> {
    val galaxyCoordinates = mutableListOf<Point>()
    for (y in universe.indices) {
        for (x in universe[y].indices) {
            if (universe[y][x] == '#') {
                galaxyCoordinates.add(Point(x, y))
            }
        }
    }
    return galaxyCoordinates
}

fun expandUniverse(universe: MutableList<MutableList<Char>>, newHorizontalPositions: List<Int>, newVerticalPositions: List<Int>): MutableList<MutableList<Char>> {
    val newUniverse = universe.toMutableList()
    var shiftVertical = 0
    var shiftHorizontal = 0
    for (verticalSpace in newVerticalPositions) {
        newUniverse.add(verticalSpace + shiftVertical, MutableList(universe[0].size) { '.' })
        shiftVertical++
    }
    for (horizontalSpace in newHorizontalPositions) {
        for (row in newUniverse) {
            if (horizontalSpace + shiftHorizontal < row.size) {
                row.add(horizontalSpace + shiftHorizontal, '.')
            } else {
                row.add('.')
            }
        }
        shiftHorizontal++
    }
    return newUniverse
}

fun day11task1() {
    val file = File("src/main/resources/day11_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    var universe = mutableListOf<MutableList<Char>>()
    for (line in lines) {
        universe.add(line.toCharArray().toMutableList())
    }
    val galaxyCoordinates = findAllGalaxiesInUniverse(universe)
    val emptyHorizontalSpaces = mutableListOf<Int>()
    val emptyVerticalSpaces = mutableListOf<Int>()
    for (y in universe.indices) {
        if (y in galaxyCoordinates.map { it.y }) {
            continue
        } else {
            emptyHorizontalSpaces.add(y)
        }
    }
    for (x in universe[0].indices) {
        if (x in galaxyCoordinates.map { it.x }) {
            continue
        } else {
            emptyVerticalSpaces.add(x)
        }
    }
    universe = expandUniverse(universe, emptyHorizontalSpaces, emptyVerticalSpaces)
    val newGalaxyCoordinates = findAllGalaxiesInUniverse(universe)
    val distances = mutableListOf<Int>()
    for (galaxyCoordinate in newGalaxyCoordinates) {
        var distance = 0
        for (otherGalaxyCoordinate in newGalaxyCoordinates) {
            if (galaxyCoordinate == otherGalaxyCoordinate) continue
            distance += getDistanceBetweenTwoPoints(galaxyCoordinate, otherGalaxyCoordinate)
        }
        distances.add(distance)
    }
    println(distances.sum() / 2)
}

fun fakeExpandUniverseOneMillionTimesMoreByChangingGalaxiesCoordinates(
    galaxyCoordinates: List<Point>,
    newHorizontalPositions: List<Int>,
    newVerticalPositions: List<Int>
): List<Point> {
    return galaxyCoordinates

    val newGalaxyCoordinates = galaxyCoordinates.toMutableList()
    var verticalShift = 0
    for (verticalSpace in newVerticalPositions) {
        newGalaxyCoordinates.forEach {
            if (it.x > verticalSpace + verticalShift) {
                it.x += 10 + verticalShift
            }
        }
        verticalShift += 10
    }
    var horizontalShift = 0
    for (horizontalSpace in newHorizontalPositions) {
        newGalaxyCoordinates.forEach {
            if (it.y > horizontalSpace + horizontalShift) {
                it.y += 10 + horizontalShift
            }
        }
        horizontalShift += 10
    }
    return galaxyCoordinates
}

fun day11task2() {
    val file = File("src/main/resources/day11_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    var universe = mutableListOf<MutableList<Char>>()
    for (line in lines) {
        universe.add(line.toCharArray().toMutableList())
    }
    val galaxyCoordinates = findAllGalaxiesInUniverse(universe)
    val emptyHorizontalSpaces = mutableListOf<Int>()
    val emptyVerticalSpaces = mutableListOf<Int>()
    for (y in universe.indices) {
        if (y in galaxyCoordinates.map { it.y }) {
            continue
        } else {
            emptyHorizontalSpaces.add(y)
        }
    }
    for (x in universe[0].indices) {
        if (x in galaxyCoordinates.map { it.x }) {
            continue
        } else {
            emptyVerticalSpaces.add(x)
        }
    }
    val newGalaxyCoordinates = fakeExpandUniverseOneMillionTimesMoreByChangingGalaxiesCoordinates(galaxyCoordinates, emptyHorizontalSpaces, emptyVerticalSpaces)
    val distances = mutableListOf<Long>()
    for (galaxyCoordinate in newGalaxyCoordinates) {
        var distance = 0L
        for (otherGalaxyCoordinate in newGalaxyCoordinates) {
            if (galaxyCoordinate == otherGalaxyCoordinate) continue
            distance += getDistanceBetweenTwoPoints(galaxyCoordinate, otherGalaxyCoordinate)
            distance += calculateDistanceAddedByEmptySpaces(galaxyCoordinate, otherGalaxyCoordinate, emptyHorizontalSpaces, emptyVerticalSpaces)
        }
        distances.add(distance)
    }
    println(distances.sum() / 2)
}

fun calculateDistanceAddedByEmptySpaces(galaxy1: Point, galaxy2: Point, emptyHorizontalSpaces: List<Int>, emptyVerticalSpaces: List<Int>): Int {
    val lowerX = if (galaxy1.x < galaxy2.x) galaxy1.x else galaxy2.x
    val higherX = if (galaxy1.x > galaxy2.x) galaxy1.x else galaxy2.x
    val lowerY = if (galaxy1.y < galaxy2.y) galaxy1.y else galaxy2.y
    val higherY = if (galaxy1.y > galaxy2.y) galaxy1.y else galaxy2.y
    val numOfEmptyVerticalSpacesBetween = emptyVerticalSpaces.filter { it in lowerX..higherX }.size
    val numOfEmptyHorizontalSpacesBetween = emptyHorizontalSpaces.filter { it in lowerY..higherY }.size
    return numOfEmptyVerticalSpacesBetween * (1000000 - 1) + numOfEmptyHorizontalSpacesBetween * (1000000 - 1)
}