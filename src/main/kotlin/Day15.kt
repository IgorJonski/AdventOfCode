import java.io.File

fun main() {
    day15task1()
}

fun day15task1() {
    val file = File("src/main/resources/day15_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val initializationSequence = file.readText()
    val sequences = initializationSequence.split(",").toList()
    var sum = 0L
    for (sequence in sequences) {
        sum += hash(sequence)
    }
    println(sum)
}

fun hash(message: String): Long {
    var hashValue = 0L
    for (character in message) {
        hashValue += character.code
        hashValue *= 17
        hashValue %= 256
    }
    return hashValue
}