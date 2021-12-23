package ninja.veearr.advent.y2021

import readFileFromResources
import java.util.*

object Dec23B {
    val MOVE_COSTS = intArrayOf(1, 10, 100, 1000)
    var NUM_EACH_TYPE = 0

    @JvmStatic
    fun main(args: Array<String>) {
        val input: List<String> = readFileFromResources("day23_data")
        solvePosition(input)

        val additionalLines = """  #D#C#B#A#
  #D#B#A#C#""".lines()
        val input2 = listOf(input.take(3), additionalLines, input.takeLast(2)).flatten()

        solvePosition(input2)
    }

    private fun solvePosition(input: List<String>) {

        NUM_EACH_TYPE = input.size - 3
        val startingPositions = IntArray(totalUnits())
        for (i in 0 until NUM_EACH_TYPE) {
            val line = input[i + 2]
            for (j in 0..3) {
                val c = line[2 * j + 3]
                var unit = (c - 'A') * NUM_EACH_TYPE
                while (startingPositions[unit] != 0) {
                    unit++
                }
                startingPositions[unit] = 4 * i + j + 7
            }
        }
        val queue: PriorityQueue<GameState> = PriorityQueue(kotlin.Comparator.comparingLong { v -> v.cost })
        queue.add(GameState(startingPositions, 0))
        var best = Long.MAX_VALUE
        val alreadyProcessed: MutableMap<String, Long> = HashMap()
        while (!queue.isEmpty()) {
            val toProcess = queue.poll()
            if (toProcess.cost >= best) {
                break
            }
            for (unit in 0 until totalUnits()) {
                val validPos = findValidPositions(toProcess.positions, unit)
                for (i in validPos.indices) {
                    if (!validPos[i]) {
                        continue
                    }
                    val price = calcPrice(unit, toProcess.positions.get(unit), i)
                    val next = toProcess.moveUnit(unit, i, price)
                    if (next.isComplete) {
                        best = Math.min(best, next.cost)
                    } else {
                        val repr = next.repr
                        if (next.cost < alreadyProcessed.getOrDefault(repr, Long.MAX_VALUE)) {
                            alreadyProcessed[repr] = next.cost
                            queue.add(next)
                        }
                    }
                }
            }
        }
        println(best)
    }

    private fun getType(unit: Int): Int {
        return if (unit == -1) {
            -1
        } else unit / NUM_EACH_TYPE
    }

    private fun totalUnits(): Int {
        return 4 * NUM_EACH_TYPE
    }

    private fun findValidPositions(positions: IntArray, unit: Int): BooleanArray {
        return if (positions[unit] < 7) {
            findValidRoomPositions(positions, unit)
        } else {
            findValidHallPositions(positions, unit)
        }
    }

    private fun findValidHallPositions(positions: IntArray, unit: Int): BooleanArray {
        val occupied = IntArray(totalUnits() + 7)
        for (i in 0 until totalUnits() + 7) {
            occupied[i] = -1
        }
        for (i in 0 until totalUnits()) {
            occupied[positions[i]] = i
        }
        val rv = BooleanArray(7)
        val cPos = positions[unit]
        val type = getType(unit)
        run {
            var i = cPos - 4
            while (i > 6) {
                if (occupied[i] > -1) {
                    return rv
                }
                i -= 4
            }
        }
        if ((cPos + 1) % 4 == type) {
            var gottaMove = false
            var i = cPos + 4
            while (i < totalUnits() + 7) {
                if (getType(occupied[i]) != type) {
                    gottaMove = true
                    break
                }
                i += 4
            }
            if (!gottaMove) {
                return rv
            }
        }
        var effPos = cPos
        while (effPos > 10) {
            effPos -= 4
        }
        for (i in 0..6) {
            if (occupied[i] == -1 && checkHallwayClear(i, effPos, occupied)) {
                rv[i] = true
            }
        }
        return rv
    }

    private fun findValidRoomPositions(positions: IntArray, unit: Int): BooleanArray {
        val occupied = IntArray(totalUnits() + 7)
        for (i in 0 until totalUnits() + 7) {
            occupied[i] = -1
        }
        for (i in 0 until totalUnits()) {
            occupied[positions[i]] = i
        }
        val rv = BooleanArray(totalUnits() + 7)
        val cPos = positions[unit]
        val type = getType(unit)
        val room1 = type + 7
        if (!checkHallwayClear(cPos, room1, occupied)) {
            return rv
        }
        var tgt = room1
        for (i in 0 until NUM_EACH_TYPE) {
            if (occupied[room1 + 4 * i] == -1) {
                tgt = room1 + 4 * i
            } else if (getType(occupied[room1 + 4 * i]) != type) {
                return rv
            }
        }
        rv[tgt] = true
        return rv
    }

    private fun checkHallwayClear(hallPos: Int, roomPos: Int, occupied: IntArray): Boolean {
        val min = Math.min(hallPos + 1, roomPos - 5)
        val max = Math.max(hallPos - 1, roomPos - 6)
        for (i in min..max) {
            if (occupied[i] != -1) {
                return false
            }
        }
        return true
    }

    private fun calcPrice(unit: Int, from: Int, to: Int): Int {
        var from = from
        var to = to
        if (from > to) {
            val tmp = from
            from = to
            to = tmp
        }
        val depth = (to - 3) / 4
        val tgtHall = (to + 1) % 4 * 2 + 3
        val discount = if (from == 0 || from == 6) 1 else 0
        val dist = Math.abs(2 * from - tgtHall) + depth - discount
        val type = getType(unit)
        return MOVE_COSTS[type] * dist
    }

    private class GameState(var positions: IntArray, var cost: Long) {
        fun moveUnit(unit: Int, position: Int, price: Int): GameState {
            val newPositions: IntArray = Arrays.copyOf(positions, positions.size)
            newPositions[unit] = position
            return GameState(newPositions, cost + price)
        }

        val isComplete: Boolean
            get() {
                for (i in positions.indices) {
                    val type = getType(i)
                    if (positions.get(i) < 7 || (positions.get(i) + 1) % 4 != type) {
                        return false
                    }
                }
                return true
            }
        val repr: String
            get() {
                val occupied = IntArray(totalUnits() + 7)
                for (i in 0 until totalUnits() + 7) {
                    occupied[i] = -1
                }
                for (i in 0 until totalUnits()) {
                    occupied[positions.get(i)] = i
                }
                var rv = ""
                for (i in 0 until totalUnits() + 7) {
                    val type = getType(occupied[i])
                    if (type == -1) {
                        rv += "x"
                    } else {
                        rv += type
                    }
                }
                return rv
            }
    }
}