import helpers.day7.CamelCardsHand
import helpers.day7.HandInfo
import java.io.File

fun main() {
//    day7Task1()
    day7Task2()
}

fun day7Task1() {
    val file = File("src/main/resources/day7_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val handInfoList = mutableListOf<HandInfo>()

    file.forEachLine { line ->
        val (handString, bidString) = line.split(" ")
        val hand = handString.toCharArray().toList()
        val bid = bidString.toInt()
        val typeOfHand = getTypeOfHand(hand)
        val cardsValues = getCardsValues(hand)
        val handInfo = HandInfo(
            hand = hand,
            bid = bid,
            typeOfHand = typeOfHand,
            firstCardValue = cardsValues[0],
            secondCardValue = cardsValues[1],
            thirdCardValue = cardsValues[2],
            fourthCardValue = cardsValues[3],
            fifthCardValue = cardsValues[4]
        )
        handInfoList.add(handInfo)
    }

    val sortedHandInfoList = handInfoList
        .asSequence()
        .sortedWith(
            compareByDescending<HandInfo> { it.typeOfHand }
                .thenBy { it.firstCardValue }
                .thenBy { it.secondCardValue }
                .thenBy { it.thirdCardValue }
                .thenBy { it.fourthCardValue }
                .thenBy { it.fifthCardValue }
        )
        .toList()

    var res = 0
    sortedHandInfoList.forEachIndexed { index, handInfo ->
        res += handInfo.bid * (index + 1)
    }

    println(res)
}

fun getTypeOfHand(hand: List<Char>): CamelCardsHand {
    return if (hand.distinct().size == 1) {
        CamelCardsHand.FIVE_OF_A_KIND
    } else if (hand.distinct().size == 2) {
        if (hand.groupingBy { it }.eachCount().values.contains(4)) {
            CamelCardsHand.FOUR_OF_A_KIND
        } else {
            CamelCardsHand.FULL_HOUSE
        }
    } else if (hand.distinct().size == 3) {
        if (hand.groupingBy { it }.eachCount().values.contains(3)) {
            CamelCardsHand.THREE_OF_A_KIND
        } else {
            CamelCardsHand.TWO_PAIR
        }
    } else if (hand.distinct().size == 4) {
        CamelCardsHand.ONE_PAIR
    } else {
        CamelCardsHand.HIGH_CARD
    }
}

fun getCardsValues(hand: List<Char>): List<Int> {
    return hand.map { card ->
        when (card) {
            'T' -> 10
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> card.toString().toInt()
        }
    }
}

fun day7Task2() {
    val file = File("src/main/resources/day7_input.txt")
    if (!file.exists()) {
        println("File not found")
        return
    }

    val handInfoList = mutableListOf<HandInfo>()

    file.forEachLine { line ->
        val (handString, bidString) = line.split(" ")
        val hand = handString.toCharArray().toList()
        val bid = bidString.toInt()
        val typeOfHand = getTypeOfHandWithJoker(hand)
        val cardsValues = getCardsValuesWithJoker(hand)
        val handInfo = HandInfo(
            hand = hand,
            bid = bid,
            typeOfHand = typeOfHand,
            firstCardValue = cardsValues[0],
            secondCardValue = cardsValues[1],
            thirdCardValue = cardsValues[2],
            fourthCardValue = cardsValues[3],
            fifthCardValue = cardsValues[4]
        )
        if (handInfo.hand.contains('J')) {
            println("${handInfo.hand} | ${handInfo.typeOfHand}")
        }
        handInfoList.add(handInfo)
    }

    val sortedHandInfoList = handInfoList
        .asSequence()
        .sortedWith(
            compareByDescending<HandInfo> { it.typeOfHand }
                .thenBy { it.firstCardValue }
                .thenBy { it.secondCardValue }
                .thenBy { it.thirdCardValue }
                .thenBy { it.fourthCardValue }
                .thenBy { it.fifthCardValue }
        )
        .toList()

    var res = 0
    sortedHandInfoList.forEachIndexed { index, handInfo ->
        res += handInfo.bid * (index + 1)
    }

    println(res)
}

fun getTypeOfHandWithJoker(hand: List<Char>): CamelCardsHand {
    return if (hand.distinct().size == 1) {
        CamelCardsHand.FIVE_OF_A_KIND  // QQQQQ JJJJJ KKKKK
    } else if (hand.distinct().size == 2) {
        if (hand.contains('J')) {  // QQQQJ QQQJJ QQJJJ QJJJJ
            CamelCardsHand.FIVE_OF_A_KIND
        } else if (hand.groupingBy { it }.eachCount().values.contains(4)) {
            CamelCardsHand.FOUR_OF_A_KIND  // QQQQK KKKKT
        } else {
            CamelCardsHand.FULL_HOUSE  // KKKTT TTKKK
        }
    } else if (hand.distinct().size == 3) {
        if (hand.contains('J')) {  // QJKKK QJJJK QQQJK QQJJK KKQJJ KQJJJ
            if (hand.groupingBy { it }.eachCount().values.contains(3)) {
                CamelCardsHand.FOUR_OF_A_KIND  // QQJKK
            } else {
                if (hand.count { it == 'J' } == 1) {
                    CamelCardsHand.FULL_HOUSE  // QQJKK QQQJK JQQQK
                } else {
                    CamelCardsHand.FOUR_OF_A_KIND  // QQJJK
                }
            }
        } else if (hand.groupingBy { it }.eachCount().values.contains(3)) {
            CamelCardsHand.THREE_OF_A_KIND  // QQQTK QTTTK QKKTK
        } else {
            CamelCardsHand.TWO_PAIR  // QKTTK QKTKT QTKKT
        }
    } else if (hand.distinct().size == 4) {
        if (hand.contains('J')) {  // QJKTK QJTKK QJTTK QJKJT QJKTK
            CamelCardsHand.THREE_OF_A_KIND
        } else {  // QTKT4 QKTK3
            CamelCardsHand.ONE_PAIR
        }
    } else {
        if (hand.contains('J')) {  // QJKT4
            CamelCardsHand.ONE_PAIR
        } else {  // QKT43
            CamelCardsHand.HIGH_CARD
        }
    }
}

fun getCardsValuesWithJoker(hand: List<Char>): List<Int> {
    return hand.map { card ->
        when (card) {
            'J' -> 1
            'T' -> 10
            'Q' -> 11
            'K' -> 12
            'A' -> 13
            else -> card.toString().toInt()
        }
    }
}