fun main() {
    part1()
    part2()
}

fun part2() {
    val matrix: Array<Array<Octopus>> = getOctopusArray()
    generateSequence(1) { it + 1 }
        .forEach { step ->
            (matrix.indices).forEach { nr ->
                matrix[nr].forEachIndexed { index, o ->
                    matrix[nr][index] = o.increaseEnergy()
                }
            }
            (matrix.indices).forEach { nr ->
                matrix[nr].forEachIndexed { idx, o ->
                    val flashing = o.isFlashing()
                    matrix[nr][idx] = flashing.first
                    if (flashing.second) {
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
    repeat(100) { _ ->
        (matrix.indices).forEach { nr ->
            matrix[nr].forEachIndexed { idx, o ->
                val octopus = o.increaseEnergy()
                matrix[nr][idx] = octopus
            }
        }
        (matrix.indices).forEach { nr ->
            matrix[nr].forEachIndexed { idx, o ->
                val oct = o.isFlashing()
                matrix[nr][idx] = oct.first
                if (oct.second) {
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
                .asSequence()
                .filter { pos -> isNotOutOfRow(pos, matrix, row) }
                .filter { pos -> isNotTheseSamePoint(row, arrayNr, pos, idx) }
                .forEach { pos ->
                    val octopus = matrix[row][pos].increaseEnergyByFlash().isFlashing()
                    matrix[row][pos] = octopus.first
                    if (octopus.second) increaseEnergyOfSurrounding(matrix, row, pos)
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


data class Octopus(val currentEnergy: Int, val numberOfFlashes: Int = 0, val alreadyFlashed: Boolean = false) {
    fun increaseEnergy(): Octopus {
        return this.copy(currentEnergy = currentEnergy + 1, alreadyFlashed = false)
    }

    fun increaseEnergyByFlash(): Octopus {
        if (!alreadyFlashed)
            return this.copy(currentEnergy = currentEnergy + 1)
        return this
    }

    fun isFlashing(): Pair<Octopus, Boolean> {
        return if (currentEnergy >= 10) {
            Pair(this.copy(currentEnergy = 0, numberOfFlashes = this.numberOfFlashes + 1, alreadyFlashed = true), true)
        } else Pair(this, false)
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
