import java.io.File

fun main() {
    day3Task1()
    day3task2()
}

fun day3Task1() {
    val file = File("src/main/resources/day3_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    var sum = 0
    val numbersWithCoordinates = HashMap<Int, List<Pair<Int, Int>>>()
    val symbolsWithCoordinates = HashMap<String, List<Pair<Int, Int>>>()

    var lineIndex = 0
    file.forEachLine { line ->
        Regex("""[0-9]+""").findAll(line).forEach { matchResult ->
            val number = matchResult.value
            val numberIndexes = line.indexesOfAllNumberOccurrences(number)
            numberIndexes.forEach { index ->
                repeat(number.length) {
                    val currentCoordinates = numbersWithCoordinates[number.toInt()] ?: listOf()
                    numbersWithCoordinates[number.toInt()] = currentCoordinates + Pair(index + it, lineIndex)
                    numbersWithCoordinates[number.toInt()] = numbersWithCoordinates[number.toInt()]!!.distinct()
                }
            }
        }

        val symbol = "*"
        val replacedLine = line.replace(Regex("""[^0-9.]"""), symbol)
        val symbolIndexes = replacedLine.indexesOfAllOccurrences(symbol)
        symbolsWithCoordinates[symbol] = symbolsWithCoordinates[symbol] ?: listOf()
        symbolIndexes.forEach { index ->
            symbolsWithCoordinates[symbol] = symbolsWithCoordinates[symbol]!! + Pair(index, lineIndex)
            symbolsWithCoordinates[symbol] = symbolsWithCoordinates[symbol]!!.distinct()
        }
        lineIndex++
    }

    val symbolCoordinates = symbolsWithCoordinates["*"] ?: listOf()
    symbolCoordinates.forEach { symbolCoordinate ->
        val indexesAround = getAllIndexesAround(symbolCoordinate)
        for (indexAround in indexesAround) {
            for (numberWithCoordinates in numbersWithCoordinates) {
                val coordinates = numberWithCoordinates.value.toMutableList()
                if (coordinates.contains(indexAround)) {
                    sum += numberWithCoordinates.key
                    var indexToRemoveLeft = Pair(indexAround.first - 1, indexAround.second)
                    var indexToRemoveRight = indexAround
                    while (coordinates.contains(indexToRemoveLeft)) {
                        coordinates.remove(indexToRemoveLeft)
                        indexToRemoveLeft = Pair(indexToRemoveLeft.first - 1, indexToRemoveLeft.second)
                    }
                    while (coordinates.contains(indexToRemoveRight)) {
                        coordinates.remove(indexToRemoveRight)
                        indexToRemoveRight = Pair(indexToRemoveRight.first + 1, indexToRemoveRight.second)
                    }
                    numbersWithCoordinates[numberWithCoordinates.key] = coordinates
                }
            }
        }
    }

    println(sum)
}

fun day3task2() {
    val file = File("src/main/resources/day3_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    var sum = 0
    val numbersWithCoordinates = HashMap<Int, List<Pair<Int, Int>>>()
    val symbolsWithCoordinates = HashMap<String, List<Pair<Int, Int>>>()

    var lineIndex = 0
    file.forEachLine { line ->
        Regex("""[0-9]+""").findAll(line).forEach { matchResult ->
            val number = matchResult.value
            val numberIndexes = line.indexesOfAllNumberOccurrences(number)
            numberIndexes.forEach { index ->
                repeat(number.length) {
                    val currentCoordinates = numbersWithCoordinates[number.toInt()] ?: listOf()
                    numbersWithCoordinates[number.toInt()] = currentCoordinates + Pair(index + it, lineIndex)
                    numbersWithCoordinates[number.toInt()] = numbersWithCoordinates[number.toInt()]!!.distinct()
                }
            }
        }

        val symbol = "*"
        val symbolIndexes = line.indexesOfAllOccurrences(symbol)
        symbolsWithCoordinates[symbol] = symbolsWithCoordinates[symbol] ?: listOf()
        symbolIndexes.forEach { index ->
            symbolsWithCoordinates[symbol] = symbolsWithCoordinates[symbol]!! + Pair(index, lineIndex)
            symbolsWithCoordinates[symbol] = symbolsWithCoordinates[symbol]!!.distinct()
        }
        lineIndex++
    }

    val symbolCoordinates = symbolsWithCoordinates["*"] ?: listOf()
    symbolCoordinates.forEach { symbolCoordinate ->
        val partNumbers = mutableListOf<Int>()
        val indexesAround = getAllIndexesAround(symbolCoordinate)
        val numbersWithCoordinatesToParse = HashMap(numbersWithCoordinates)
        for (indexAround in indexesAround) {
            for (numberWithCoordinates in numbersWithCoordinatesToParse) {
                val coordinates = numberWithCoordinates.value.toMutableList()
                if (coordinates.contains(indexAround)) {
                    partNumbers.add(numberWithCoordinates.key)
                    var indexToRemoveLeft = Pair(indexAround.first - 1, indexAround.second)
                    var indexToRemoveRight = indexAround
                    while (coordinates.contains(indexToRemoveLeft)) {
                        coordinates.remove(indexToRemoveLeft)
                        indexToRemoveLeft = Pair(indexToRemoveLeft.first - 1, indexToRemoveLeft.second)
                    }
                    while (coordinates.contains(indexToRemoveRight)) {
                        coordinates.remove(indexToRemoveRight)
                        indexToRemoveRight = Pair(indexToRemoveRight.first + 1, indexToRemoveRight.second)
                    }
                    numbersWithCoordinatesToParse[numberWithCoordinates.key] = coordinates
                }
            }
        }
        if (partNumbers.size == 2) {
            sum += partNumbers[0] * partNumbers[1]
        }
    }

    println(sum)
}

private fun String.indexesOfAllOccurrences(toFind: String): ArrayList<Int> {
    val indexes = ArrayList<Int>()
    var index = indexOf(toFind)
    while (index >= 0) {
        indexes.add(index)
        index = indexOf(toFind, index + 1)
    }
    return indexes
}

private fun String.indexesOfAllNumberOccurrences(toFind: String): List<Int> {
    val regex = """\b$toFind\b""".toRegex()
    val matches = regex.findAll(this)
    return matches.map { it.range.first }.toList()
}

private fun getAllIndexesAround(coordinate: Pair<Int, Int>): List<Pair<Int, Int>> {
    val (x, y) = coordinate
    return listOf(
        Pair(x - 1, y - 1),
        Pair(x, y - 1),
        Pair(x + 1, y - 1),
        Pair(x - 1, y),
        Pair(x + 1, y),
        Pair(x - 1, y + 1),
        Pair(x, y + 1),
        Pair(x + 1, y + 1)
    )
}