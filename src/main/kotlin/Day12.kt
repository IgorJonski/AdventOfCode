import java.io.File

fun main() {
//    day12task1()
    day12task2()
}

fun day12task1() {
    val file = File("src/main/resources/day12_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val brokenRecords = mutableListOf<String>()
    val engineerNumberNotes = mutableListOf<List<Int>>()
    for (line in lines) {
        val (record, engineerNumberNote) = line.split(" ")
        brokenRecords.add(record)
        engineerNumberNotes.add(engineerNumberNote.split(",").map { it.toInt() })
    }

    var sum = 0
    for (i in lines.indices) {
        val combinations = mutableListOf<String>()
        val record = brokenRecords[i]
        val engineerNumberNote = engineerNumberNotes[i]
        val queue = mutableListOf(record)
        while (queue.isNotEmpty()) {
            val item = queue.removeFirst()
            if (!item.contains("?")) {
                combinations.add(item)
            }
            else {
                queue.add(item.replaceFirst("?", "#"))
                queue.add(item.replaceFirst("?", "."))
            }
        }
        val sumInLine = combinations.count {
            Regex("#+").findAll(it).map { it.value.length }.toList() == engineerNumberNote
        }
        sum += sumInLine
    }
    println(sum)
}

fun day12task2() {
    val file = File("src/main/resources/day12_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines().toMutableList()
    for (i in lines.indices) {
        val brokenPart = lines[i].split(" ").first()
        val notePart = lines[i].split(" ").last()
        val newBrokenPart = "$brokenPart?".repeat(5).dropLast(1)
        val newNotePart = "$notePart,".repeat(5).dropLast(1)
        lines[i] = "$newBrokenPart $newNotePart"
    }

    val brokenRecords = mutableListOf<String>()
    val engineerNumberNotes = mutableListOf<List<Int>>()
    for (line in lines) {
        val (record, engineerNumberNote) = line.split(" ")
        brokenRecords.add(record)
        engineerNumberNotes.add(engineerNumberNote.split(",").map { it.toInt() })
    }

    var sum = 0
    for (i in lines.indices) {
        println("Line $i (${lines[i]})")
        val combinations = mutableListOf<String>()
        val record = brokenRecords[i]
        val engineerNumberNote = engineerNumberNotes[i]
        val queue = mutableListOf(record)
        while (queue.isNotEmpty()) {
            val item = queue.removeFirst()
            if (!item.contains("?")) {
                combinations.add(item)
            }
            else {
                queue.add(item.replaceFirst("?", "#"))
                queue.add(item.replaceFirst("?", "."))
            }
        }
        val sumInLine = combinations.count {
            Regex("#+").findAll(it).map { it.value.length }.toList() == engineerNumberNote
        }
        sum += sumInLine
    }
    println(sum)
}