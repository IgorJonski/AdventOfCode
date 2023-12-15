package helpers.day7

data class HandInfo(
    val hand: List<Char>,
    val bid: Int,
    val typeOfHand: CamelCardsHand,
    val firstCardValue: Int,
    val secondCardValue: Int,
    val thirdCardValue: Int,
    val fourthCardValue: Int,
    val fifthCardValue: Int
)