import java.util.*


private val startingToEndingChar = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
private val illegalCharWeight = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val legalCharWeight = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

fun main() {
    val lines = readFileFromResources("day10_data")
    val lineValidator = LineValidator(lines)
    println(lineValidator.part1())
    println(lineValidator.part2())
}

class LineValidator(private val lines: List<String>) {
    fun part1() = lines.sumOf { l ->
        val openingCharsList = LinkedList<Char>()
        l.sumOf { c ->
            when {
                startingToEndingChar.containsKey(c) -> {
                    openingCharsList.addLast(c)
                    0
                }
                c != startingToEndingChar[openingCharsList.removeLast()] -> illegalCharWeight[c]
                else -> 0
            }
        }
    }

    fun part2() = lines.mapNotNull {
        val parsed = LinkedList<Char>()
        for (c in it) when {
            startingToEndingChar.containsKey(c) -> parsed.addLast(startingToEndingChar[c])
            c != parsed.removeLast() -> return@mapNotNull null
        }
        parsed.foldRight(0L) { c, partial -> (legalCharWeight[c]!! + partial * 5) }
    }
        .sorted()
        .let {
            it[it.size / 2]
        }
}