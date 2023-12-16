import java.io.File

fun main() {
//    day8task1()
    day8task2()
}

fun day8task1() {
    val file = File("src/main/resources/day8_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val map = mutableMapOf<String, Pair<String, String>>()
    val directions: List<Char> = file.bufferedReader().use { it.readLine() }.toCharArray().toList()
    var lineIndex = 0
    file.forEachLine { line ->
        if (lineIndex < 2) {
            lineIndex++
            return@forEachLine
        }
        val key = line.substringBefore(" =")
        val value = line.substringAfter("= (").substringBefore(")").split(", ")
        map[key] = Pair(value[0], value[1])
    }

    var step = 0
    var totalSteps = 0
    val directionsSize = directions.size
    var currentPlace = "AAA"

    while (true) {
        if (currentPlace == "ZZZ") {
            break
        }
        val currentDirection = directions[step]
        val currentPlacePair = map[currentPlace]!!
        val nextPlace = if (currentDirection == 'L') currentPlacePair.first else currentPlacePair.second
        currentPlace = nextPlace
        step = (step + 1) % directionsSize
        totalSteps++
    }

    println(totalSteps)
}

fun day8task2() {
    val file = File("src/main/resources/day8_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val map = mutableMapOf<String, Pair<String, String>>()
    val directions: List<Char> = file.bufferedReader().use { it.readLine() }.toCharArray().toList()
    val startingPoints = mutableListOf<String>()
    var lineIndex = 0
    file.forEachLine { line ->
        if (lineIndex < 2) {
            lineIndex++
            return@forEachLine
        }
        val key = line.substringBefore(" =")
        val value = line.substringAfter("= (").substringBefore(")").split(", ")
        map[key] = Pair(value[0], value[1])
        if (key.endsWith("A")) {
            startingPoints.add(key)
        }
    }

    val stepsForEachStartingPoint = MutableList(startingPoints.size) { 0 }

    var step = 0
    val directionsSize = directions.size
    var startingPointIndex = 0
    startingPoints.forEach { startingPoint ->
        var currentPlace = startingPoint
        var currentPoint = map[startingPoint]!!
        while (!currentPlace.endsWith('Z')) {
            val currentDirection = directions[step]
            val nextPlace = if (currentDirection == 'L') currentPoint.first else currentPoint.second
            currentPlace = nextPlace
            currentPoint = map[currentPlace]!!
            step = (step + 1) % directionsSize
            stepsForEachStartingPoint[startingPointIndex]++
        }
        startingPointIndex++
    }

    var res: Long = stepsForEachStartingPoint[0].toLong()
    stepsForEachStartingPoint.forEach { steps ->
        res = lcm(res, steps.toLong())
    }
    print(res)
}

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}