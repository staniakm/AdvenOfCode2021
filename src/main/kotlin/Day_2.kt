fun main() {
    val startPosition1 = Position(0, 0, 0)
    val result1 = day2SampleData()
        .fold(startPosition1) { acc, newPosition -> Move(acc, newPosition).changePosition() }
        .result()

    println(result1)


    val startPosition2 = Position(0, 0, 0)
    val result2 = day2SampleData()
        .fold(startPosition2) { position, newPosition -> Move(position, newPosition).changePositionWithAim() }
        .result()

    println(result2)
}


data class Position(val horizontal: Int, val vertical: Int, val aim: Int) {
    fun result(): Int = horizontal * vertical
}

data class Move(val position: Position, val input: String) {

    fun changePosition(): Position {
        val s = input.split(" ")
        return when (s[0]) {
            "forward" -> position.copy(horizontal = position.horizontal + s[1].toInt())
            "up" -> position.copy(vertical = position.vertical - s[1].toInt())
            "down" -> position.copy(vertical = position.vertical + s[1].toInt())
            else -> Position(0, 0, 0)
        }
    }

    fun changePositionWithAim(): Position {
        val s = input.split(" ")
        return when (s[0]) {
            "forward" -> position.copy(
                horizontal = position.horizontal + s[1].toInt(),
                vertical = position.vertical + s[1].toInt() * position.aim
            )
            "up" -> position.copy(aim = position.aim - s[1].toInt())
            "down" -> position.copy(aim = position.aim + s[1].toInt())
            else -> Position(0, 0, 0)
        }
    }
}

fun day2SampleData() = readFileFromResources("day2_data")

