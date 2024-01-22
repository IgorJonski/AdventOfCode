import java.io.File
import kotlin.math.max

fun main() {
    day6task1()
    day6task2()
}

fun day6task1() {
    val file = File("src/main/resources/day6_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val times = lines.first().substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val distances = lines.last().substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val races = times.zip(distances)

    val waysToBeatRecordForEachRace = MutableList(races.size) { 1 }
    var index = 0
    races.forEach {
        val time = it.first
        val distance = it.second
        for (buttonPressedTime in 1..time) {
            val speed = buttonPressedTime
            val timeLeftAfterReleasingButton = time - buttonPressedTime
            val distanceTravelled = timeLeftAfterReleasingButton * speed
            if (distanceTravelled > distance) {
                waysToBeatRecordForEachRace[index]++
            }
        }
        index++
    }
    var res = 1
    waysToBeatRecordForEachRace.forEach {
        res *= max(it - 1, 1)
    }
    println(res)
}

fun day6task2() {
    val file = File("src/main/resources/day6_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val time = lines.first().substringAfter(":").split(" ").filter { it.isNotBlank() }.joinToString("")
    val distance = lines.last().substringAfter(":").split(" ").filter { it.isNotBlank() }.joinToString("")
    val times = listOf(time.toLong())
    val distances = listOf(distance.toLong())
    val races = times.zip(distances)

    val waysToBeatRecordForEachRace = MutableList(races.size) { 1 }
    var index = 0
    races.forEach {
        val time = it.first
        val distance = it.second
        for (buttonPressedTime in 1..time) {
            val speed = buttonPressedTime
            val timeLeftAfterReleasingButton = time - buttonPressedTime
            val distanceTravelled = timeLeftAfterReleasingButton * speed
            if (distanceTravelled > distance) {
                waysToBeatRecordForEachRace[index]++
            }
        }
        index++
    }
    var res = 1
    waysToBeatRecordForEachRace.forEach {
        res *= max(it - 1, 1)
    }
    println(res)
}