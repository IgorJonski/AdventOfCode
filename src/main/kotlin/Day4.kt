import java.io.File
import kotlin.math.pow

fun main() {
    day4task1()
    day4task2()
}

fun day4task1() {
    val file = File("src/main/resources/day4_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    var sum = 0
    file.forEachLine { line ->
        val winningNumbers = line
            .substringAfter(":")
            .substringBefore("|")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
        val myNumbers = line
            .substringAfter("|")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
        val myNumbersThatWon = myNumbers.filter { winningNumbers.contains(it) }
        sum += 2.0.pow(myNumbersThatWon.size.toDouble() - 1).toInt()
    }

    println(sum)
}

fun day4task2() {
    val file = File("src/main/resources/day4_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val numberOfRows = 220  // my given input number of rows
    val instances = MutableList(numberOfRows) { 1 }
    var index = 1
    var sum = 0
    file.forEachLine { line ->
        repeat(instances[index]) {
            val winningNumbers = line
                .substringAfter(":")
                .substringBefore("|")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
            val myNumbers = line
                .substringAfter("|")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
            val myNumbersThatWon = myNumbers.filter { winningNumbers.contains(it) }
            for (i in myNumbersThatWon.indices) {
                instances[index + i + 1]++
            }
        }
        index++
    }
    for (i in 1..<instances.size) {
        sum += instances[i]
    }

    println(sum)
}