import java.io.File
import kotlin.math.max

fun main() {
    day2Task1()
    day2Task2()
}

fun day2Task1() {
    val file = File("src/main/resources/day2_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val maxRedCubes = 12
    val maxGreenCubes = 13
    val maxBlueCubes = 14

    var allGamesIdSum = 0
    var impossibleGamesIdSum = 0

    file.forEachLine { line ->
        val gameId = line.substringBefore(":").split(" ").last()
        allGamesIdSum += gameId.toInt()
        val games = line.substringAfter(": ").split("; ")
        for (game in games) {
            var redCubes = 0
            var greenCubes = 0
            var blueCubes = 0
            val specificCubes = game.split(", ")
            specificCubes.forEach { colorCubes ->
                val (count, color) = colorCubes.split(" ")
                when (color) {
                    "red" -> {
                        redCubes += count.toInt()
                    }
                    "green" -> {
                        greenCubes += count.toInt()
                    }
                    "blue" -> {
                        blueCubes += count.toInt()
                    }
                }
            }
            if (redCubes > maxRedCubes || greenCubes > maxGreenCubes || blueCubes > maxBlueCubes) {
                impossibleGamesIdSum += gameId.toInt()
                break
            }
        }
    }

    val possibleGamesIdSum = allGamesIdSum - impossibleGamesIdSum
    println(possibleGamesIdSum)
}

fun day2Task2() {
    val file = File("src/main/resources/day2_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    var sumOfPowerOfSetsOfCubes = 0
    file.forEachLine { line ->
        val games = line.substringAfter(": ").split("; ")
        var requiredRedCubes = 0
        var requiredGreenCubes = 0
        var requiredBlueCubes = 0
        games.forEach {game ->
            val specificCubes = game.split(", ")
            specificCubes.forEach { colorCubes ->
                val (count, color) = colorCubes.split(" ")
                when (color) {
                    "red" -> {
                        requiredRedCubes = max(requiredRedCubes, count.toInt())
                    }
                    "green" -> {
                        requiredGreenCubes = max(requiredGreenCubes, count.toInt())
                    }
                    "blue" -> {
                        requiredBlueCubes = max(requiredBlueCubes, count.toInt())
                    }
                }
            }
        }
        val powerOfSetOfCubes = requiredRedCubes * requiredGreenCubes * requiredBlueCubes
        sumOfPowerOfSetsOfCubes += powerOfSetOfCubes
    }
    println(sumOfPowerOfSetsOfCubes)
}