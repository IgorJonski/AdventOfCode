import java.io.File

fun main() {
    task1()
    task2()
}

fun task1() {
    val file = File("src/main/resources/day1_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }
    var sum = 0
    file.forEachLine { line ->
        val digits = line.filter { it.isDigit() }.map { it.digitToInt() }
        if (digits.isNotEmpty()) {
            sum += "${digits.first()}${digits.last()}".toInt()
        }
    }
    println("Task 1: $sum")
}

fun task2() {
    val digitsSpelledOut = listOf(
        "zero", "one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine"
    )
    val file = File("src/main/resources/day1_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }
    var sum = 0
    file.forEachLine { line ->
        val parsedDigits = HashMap<Int, Int>()
        digitsSpelledOut.forEachIndexed { index, digit ->
            if (line.contains(digit)) {
                val firstIndex = line.indexOf(digit)
                val lastIndex = line.lastIndexOf(digit)
                parsedDigits[firstIndex] = index
                parsedDigits[lastIndex] = index
            }
        }
        line.mapIndexed { index, c ->
            if (c.isDigit()) {
                parsedDigits[index] = c.digitToInt()
            }
        }
        parsedDigits.toSortedMap().values.toList().let {
            sum += "${it.first()}${it.last()}".toInt()
        }
    }
    println("Task 2: $sum")
}