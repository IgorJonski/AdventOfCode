import java.io.File

fun main() {
//    day5task1()
    day5task2()
}

data class Mapping(val destinationRange: Long, val sourceRange: Long, val rangeLength: Long)
data class Categories(val from: String, val to: String, val mappings: MutableList<Mapping>)
data class SeedRange(val from: Long, val to: Long)

fun day5task1() {
    val file = File("src/main/resources/day5_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val lines = file.readLines()
    val seeds = mutableListOf<Long>()
    val categories = mutableListOf<Categories>()
    for (line in lines) {
        if (seeds.isEmpty()) {
            seeds.addAll(line.substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toLong() })
            continue
        }
        if (line.isBlank()) continue
        if (!line.contains("[0-9]".toRegex())) {  // header of categories
            val from = line.substringBefore("-to")
            val to = line.substringAfter("to-").substringBefore(" map")
            categories.add(Categories(from, to, mutableListOf()))
        } else {  // mappings of categories
            val (destinationRange, sourceRange, rangeLength) = line.split(" ").map { it.toLong() }
            categories.last().mappings.add(Mapping(destinationRange, sourceRange, rangeLength))
        }
    }

    val locations = mutableListOf<Long>()
    for (seed in seeds) {
        locations.add(getLowestLocationValueForSeed(seed, categories))
    }
    println("Lowest location value: ${locations.minOrNull()}")
}

fun getLowestLocationValueForSeed(seed: Long, categories: List<Categories>): Long {
    var value = seed
    for (category in categories) {
        for (mapping in category.mappings) {
            if (value in mapping.sourceRange..<(mapping.sourceRange + mapping.rangeLength)) {
                value = mapping.destinationRange + (value - mapping.sourceRange)
                break
            }
        }
    }
    return value
}

fun day5task2() {
    val file = File("src/main/resources/day5_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    var lowestLocationValue = Long.MAX_VALUE
    val lines = file.readLines()
    val categories = mutableListOf<Categories>()
    for (line in lines) {
        if (line.isBlank() || line.contains("seeds:")) continue
        if (!line.contains("[0-9]".toRegex())) {  // header of categories
            val from = line.substringBefore("-to")
            val to = line.substringAfter("to-").substringBefore(" map")
            categories.add(Categories(from, to, mutableListOf()))
        } else {  // mappings of categories
            val (destinationRange, sourceRange, rangeLength) = line.split(" ").map { it.toLong() }
            categories.last().mappings.add(Mapping(destinationRange, sourceRange, rangeLength))
        }
    }

    val seeds = lines.first().substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val seedsRanges = mutableListOf<SeedRange>()
    var countSeeds = 0
    for (i in seeds.indices step 2) {
        seedsRanges.add(SeedRange(seeds[i], seeds[i] + seeds[i + 1]))
    }
    for (seedRange in seedsRanges) {
        for (seed in seedRange.from..<seedRange.to) {
            val locationValue = getLowestLocationValueForSeed(seed, categories)
            if (locationValue < lowestLocationValue) {
                lowestLocationValue = locationValue
                println("New lowest location for seed $seed: $lowestLocationValue")
            }
            countSeeds++
        }
    }
    println("Lowest location value: $lowestLocationValue")
}