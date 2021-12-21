import java.lang.Long.max

fun main() {

    val input = Day21(3, 7)
    println(input.part1())

    val test2 = Day21Quantum(3, 7)
    println(test2.part2())
}

private class Day21(firstPlayerPosition: Int, secondPlayerPosition: Int) {

    private val die = Die()

    private data class Player(var position: Int, var score: Int = 0) {
        fun advance(increment: Int) {
            position = (position + increment - 1) % 10 + 1
            score += position
        }
    }

    private val firstPlayer = Player(firstPlayerPosition)
    private val secondPlayer = Player(secondPlayerPosition)


    private class Die {
        var randomValue = 1
        var rolled = 0

        fun roll(): Int {
            rolled++
            val totallyRandomValue = randomValue
            randomValue = (randomValue % 100) + 1
            return totallyRandomValue
        }
    }

    fun runUntilScore(score: Int) {
        while (true) {
            firstPlayer.advance(die.roll() + die.roll() + die.roll())
            if (firstPlayer.score >= score) break
            secondPlayer.advance(die.roll() + die.roll() + die.roll())
            if (secondPlayer.score >= score) break
        }
    }

    fun part1(): Int {
        runUntilScore(1000)
        println("${die.rolled} -> ${firstPlayer.score} -- ${secondPlayer.score}")
        return die.rolled * if (firstPlayer.score > secondPlayer.score) secondPlayer.score else firstPlayer.score
    }

}

const val quantumWinningScore = 21

private class Day21Quantum(firstPlayerPosition: Int, secondPlayerPosition: Int) {

    private data class Player(val position: Int, val score: Int = 0) {
        fun advance(increment: Int): Player {
            val newPosition = (position + increment - 1) % 10 + 1
            return Player(newPosition, score + newPosition)
        }
    }

    private val dieOutcomes = 3..9

    private fun outcomesCount(outcome: Int): Int {
        return when (outcome) {
            3 -> 1
            4 -> 3
            5 -> 6
            6 -> 7
            7 -> 6
            8 -> 3
            9 -> 1
            else -> 0
        }
    }

    private data class Universe(val firstPlayer: Player, val secondPlayer: Player, val rolls: Int = 0) {
        fun isGameFinished(): Boolean = firstPlayerWon() || secondPlayerWon()
        fun firstPlayerWon(): Boolean = firstPlayer.score >= quantumWinningScore
        fun secondPlayerWon(): Boolean = secondPlayer.score >= quantumWinningScore
    }

    private fun firstPlayerTurn(universe: Universe): List<Pair<Int, Universe>> {
        if (universe.isGameFinished()) {
            throw Error("This universe has finished playing")
        }
        return dieOutcomes.map { outcomesCount(it) }.zip(dieOutcomes.map {
            Universe(
                universe.firstPlayer.advance(it),
                universe.secondPlayer,
                universe.rolls + 3
            )
        })
    }

    private fun secondPlayerTurn(universe: Universe): List<Pair<Int, Universe>> {
        if (universe.isGameFinished()) {
            throw Error("This universe has finished playing")
        }
        return dieOutcomes.map { outcomesCount(it) }.zip(dieOutcomes.map {
            Universe(
                universe.firstPlayer,
                universe.secondPlayer.advance(it),
                universe.rolls + 3
            )
        })
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun oneTurn(universe: Universe): List<Pair<Int, Universe>> {
        val afterPlayerOne = firstPlayerTurn(universe)
        return buildList {
            for (u in afterPlayerOne) {
                if (u.second.isGameFinished()) add(u)
                else addAll(secondPlayerTurn(u.second).map { Pair(u.first * it.first, it.second) })
            }
        }
    }

    private val multiverse = hashMapOf(Universe(Player(firstPlayerPosition), Player(secondPlayerPosition)) to 1L)

    private fun oneStep(): Boolean {
        val ongoingUniverses = multiverse.keys.filterNot { it.isGameFinished() }
        for (universe in ongoingUniverses) {
            val newUniverses = oneTurn(universe)
            val count = multiverse.getValue(universe)
            multiverse.remove(universe)
            for (nu in newUniverses) {
                multiverse[nu.second] = (multiverse[nu.second] ?: 0L) + count * nu.first
            }
        }
        return multiverse.keys.filterNot { it.isGameFinished() }.isNotEmpty()
    }

    fun part2(): Long {
        while (oneStep()) {
        }
        val firstWinning = multiverse.filter { it.key.firstPlayerWon() }.values.sum()
        val secondWinning = multiverse.filter { it.key.secondPlayerWon() }.values.sum()
        return max(firstWinning, secondWinning)
    }

}