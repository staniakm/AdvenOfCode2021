fun main() {
    val bingo: List<Int> = parseNumbers(day4SampleData().first())
    val boards: List<Board> = day4SampleData().filter { it.isNotBlank() }.drop(1)
        .windowed(5, 5)
        .map {
            loadBoard(it)
        }

    val result = playFirstWon(bingo, boards)
    println("first won: $result")
    check(16716 == result)

    val lastWonResult = playLastWon(bingo, boards)
    println("Last won: $lastWonResult")
    check(4880 == lastWonResult)
}

private fun playFirstWon(bingo: List<Int>, boards: List<Board>): Int {

    return bingo
        .asSequence()
        .filter { _ -> boards.any { !it.won() } }
        .map { number ->
            boards.filter { it.notWonYet() }
                .filter { it.isBingo(number) }
                .map {
                    it.calculateResult(number)
                }
        }
        .first { it.isNotEmpty() }
        .first()
}

private fun playLastWon(bingo: List<Int>, boards: List<Board>): Int {
    return bingo
        .map { number ->
            boards.filter { it.notWonYet() }
                .filter { it.isBingo(number) }
                .map {
                    it.calculateResult(number)
                }
        }.last { it.isNotEmpty() }
        .last ()
}

fun parseNumbers(input: String): List<Int> {
    return input.split(",").filter { it.isNotBlank() }.map { it.toInt() }
}

fun loadBoard(boardData: List<String>): Board {
    return Board().parseInputData(boardData)
}

class Board() {
    private val row: MutableList<BoardRow> = mutableListOf()
    private val boardSize = 5
    private var alreadyWon = false

    fun parseInputData(strings: List<String>): Board {
        val rowsSet: MutableList<BoardRow> = strings
            .map {
                it.split(" ").filter { it.isNotBlank() }
                    .map { number ->
                        BoardPosition(number.toInt())
                    }.let { rowData ->
                        BoardRow(rowData)
                    }
            }.toMutableList()
        row.addAll(rowsSet)
        return this
    }

    fun isBingo(number: Int): Boolean {
        row.forEach {
            it.markNumber(number)
        }
        return checkIfBingo()
    }

    private fun checkIfBingo(): Boolean {
        alreadyWon = (checkRowsForBingo() || checkColumnsForBingo())
        return alreadyWon
    }

    private fun checkColumnsForBingo(): Boolean {
        var index = 0
        while (index < boardSize - 1) {
            val isColumnBingo = row.map {
                it.getColum(index)
            }.all {
                it.isMarked()
            }
            if (isColumnBingo) {
                return true
            }
            index++
        }
        return false
    }

    private fun checkRowsForBingo(): Boolean {
        return row.any { r -> r.isBingo() }
    }


    override fun toString(): String {
        return "Board(row=$row)\n"
    }

    fun calculateResult(number: Int): Int {
        return number * row.sumOf {
            it.getSumAllUnmarked()
        }
    }

    fun notWonYet(): Boolean = !alreadyWon
    fun won() = alreadyWon
}

data class BoardRow(val row: List<BoardPosition>) {
    fun markNumber(number: Int) {
        row.forEach {
            it.markNumber(number)
        }
    }

    fun isBingo(): Boolean {
        return row.all {
            it.isMarked()
        }
    }

    fun getSumAllUnmarked(): Int {
        return row.filter {
            !it.isMarked()
        }.sumOf {
            it.number
        }
    }

    fun getColum(index: Int): BoardPosition {
        return row[index]
    }
}

data class BoardPosition(val number: Int, private var marked: Boolean = false) {
    fun markNumber(bingoNUmber: Int) {
        if (bingoNUmber == number) {
            marked = true
        }
    }

    fun isMarked(): Boolean {
        return marked
    }

}

fun day4SampleData() = readFileFromResources("day4_data")

fun sampleData() = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7""".lines()
