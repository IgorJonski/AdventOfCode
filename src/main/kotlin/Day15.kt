import java.io.File

fun main() {
//    day15task1()
    day15task2()
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

fun day15task2() {
    val file = File("src/main/resources/day15_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val initializationSequence = file.readText()
    val sequences = initializationSequence.split(",").toList()
    val boxes = MutableList<MutableList<LensSlot>>(256) { mutableListOf() }
    for (sequence in sequences) {
        if (sequence.contains("=")) {
            val label = sequence.substringBefore("=")
            val focalLength = sequence.substringAfter("=").toLong()
            val hash = hash(label).toInt()
            val existingLensSlot = boxes[hash].firstOrNull { it.label == label }
            if (existingLensSlot != null) {
                existingLensSlot.focalLength = focalLength
            } else {
                boxes[hash].add(LensSlot(label, focalLength))
            }
        } else {
            val label = sequence.substringBefore("-")
            val hash = hash(label).toInt()
            boxes[hash].removeIf { it.label == label }
        }
    }

    var sum = 0L
    for (boxIndex in boxes.indices) {
        for (lensSlotIndex in boxes[boxIndex].indices) {
            val onePlusBoxIndex = boxIndex + 1
            val slotNumber = lensSlotIndex + 1
            val focalLength = boxes[boxIndex][lensSlotIndex].focalLength
            sum += onePlusBoxIndex * slotNumber * focalLength
        }
    }

    println(sum)
}

data class LensSlot(val label: String, var focalLength: Long)