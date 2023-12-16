import java.io.File

fun main() {
    day9task1()
    day9task2()
}

fun day9task1() {
    val file = File("src/main/resources/day9_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val numbersLists = mutableListOf<MutableList<Int>>()
    val extrapolations = mutableListOf<Int>()
    file.forEachLine { line ->
        var numbers = line.split(" ").map { it.toInt() }
        numbersLists.add(numbers.toMutableList())
        while (true) {
            var prevIndex = 0
            var nextIndex = 1
            val newNumbers = mutableListOf<Int>()
            while (nextIndex < numbers.size) {
                val prevNumber = numbers[prevIndex]
                val nextNumber = numbers[nextIndex]
                val newNumberBelow = nextNumber - prevNumber
                newNumbers.add(newNumberBelow)
                prevIndex++
                nextIndex++
            }
            numbersLists.add(newNumbers)
            numbers = newNumbers
            if (numbers.count { it == 0 } == numbers.size) {
                extrapolations.add(extrapolate(numbersLists))
                numbersLists.clear()
                return@forEachLine
            }
        }
    }

    println(extrapolations.sum())
}

fun extrapolate(numbers: MutableList<MutableList<Int>>): Int {
    numbers.reverse()
    numbers[0].add(0)
    var newValue = 0
    for (i in 1..<numbers.size) {
        val row = numbers[i]
        val lastNumber = row[row.size - 1]
        val x = lastNumber + newValue
        newValue = x
        row.add(newValue)
    }
    return newValue
}

fun day9task2() {
    val file = File("src/main/resources/day9_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val numbersLists = mutableListOf<MutableList<Int>>()
    val extrapolations = mutableListOf<Int>()
    file.forEachLine { line ->
        var numbers = line.split(" ").map { it.toInt() }
        numbersLists.add(numbers.toMutableList())
        while (true) {
            var prevIndex = 0
            var nextIndex = 1
            val newNumbers = mutableListOf<Int>()
            while (nextIndex < numbers.size) {
                val prevNumber = numbers[prevIndex]
                val nextNumber = numbers[nextIndex]
                val newNumberBelow = nextNumber - prevNumber
                newNumbers.add(newNumberBelow)
                prevIndex++
                nextIndex++
            }
            numbersLists.add(newNumbers)
            numbers = newNumbers
            if (numbers.count { it == 0 } == numbers.size) {
                extrapolations.add(extrapolateBackwards(numbersLists))
                numbersLists.clear()
                return@forEachLine
            }
        }
    }
    println(extrapolations.sum())
}

fun extrapolateBackwards(numbers: MutableList<MutableList<Int>>): Int {
    numbers.reverse()
    numbers[0].add(0, 0)
    var newValue = 0
    for (i in 1..<numbers.size) {
        val row = numbers[i]
        val firstNumber = row[0]
        val x = firstNumber - newValue
        newValue = x
        row.add(0, newValue)
    }
    return newValue
}