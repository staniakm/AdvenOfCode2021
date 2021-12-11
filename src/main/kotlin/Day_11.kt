fun main() {


    part1()
    part2()

}

fun part2() {
    val matrix: Array<Array<Octopus>> = getOctopusArray()
    generateSequence(1) { it + 1 }
        .asSequence().forEach { step ->
            (matrix.indices).forEach { nr ->
                matrix[nr].forEachIndexed { _, o ->
                    o.increaseEnergy()
                }
            }
            (matrix.indices).forEach { nr ->
                matrix[nr].forEachIndexed { idx, o ->
                    if (o.isFlashing()) {
                        increaseEnergyOfSurrounding(matrix, nr, idx)
                    }
                }
            }
            val count = matrix.sumOf { out ->
                out.count {
                    it.currentEnergy > 0
                }
            }
            if (count == 0) {
                println("all flashes at step: $step")
                return
            }
        }
}

fun part1() {
    val matrix: Array<Array<Octopus>> = getOctopusArray()
    (1..100).forEach { _ ->
        (matrix.indices).forEach { nr ->
            matrix[nr].forEachIndexed { _, o ->
                o.increaseEnergy()
            }
        }
        (matrix.indices).forEach { nr ->
            matrix[nr].forEachIndexed { idx, o ->
                if (o.isFlashing()) {
                    increaseEnergyOfSurrounding(matrix, nr, idx)
                }
            }
        }
    }
    val map = matrix.sumOf { out ->
        out.sumOf {
            it.numberOfFlashes
        }
    }

    println("numberOfFlashes: $map")
    check(1642 == map)
}

fun increaseEnergyOfSurrounding(matrix: Array<Array<Octopus>>, arrayNr: Int, idx: Int) {
    (arrayNr - 1..arrayNr + 1)
        .filter { row -> row >= 0 && row <= matrix.size - 1 }
        .forEach { row ->
            (idx - 1..idx + 1)
                .filter { pos -> isNotOutOfRow(pos, matrix, row) }
                .filter { pos -> isNotTheseSamePoint(row, arrayNr, pos, idx) }
                .forEach { pos ->
                    matrix[row][pos].apply {
                        increaseEnergyByFlash()
                    }.also {
                        if (it.isFlashing()) increaseEnergyOfSurrounding(matrix, row, pos)
                    }
                }
        }
}

private fun isNotOutOfRow(
    pos: Int,
    matrix: Array<Array<Octopus>>,
    row: Int
) = pos >= 0 && pos <= matrix[row].size - 1

private fun isNotTheseSamePoint(
    row: Int,
    arrayNr: Int,
    pos: Int,
    idx: Int
) = !(row == arrayNr && pos == idx)


data class Octopus(var currentEnergy: Int, var numberOfFlashes: Int = 0) {
    private var alreadyFlashed = false
    fun increaseEnergy() {
        alreadyFlashed = false
        currentEnergy++
    }

    fun increaseEnergyByFlash() {
        if (!alreadyFlashed)
            currentEnergy++
    }

    fun isFlashing(): Boolean {
        return if (currentEnergy >= 10) {
            numberOfFlashes++
            currentEnergy = 0
            alreadyFlashed = true
            true
        } else false
    }

    override fun toString(): String {
        return "($currentEnergy)"
    }
}

fun getOctopusArray() = readFileFromResources("day11_data")
    .map { line ->
        line.split("").filter { it.isNotBlank() }.map {
            Octopus(it.toInt(), 0)
        }.toTypedArray()
    }.toTypedArray()
