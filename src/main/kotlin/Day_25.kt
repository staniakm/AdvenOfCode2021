fun main() {
    val input: List<String> = readFileFromResources("day25_data")
    val grid = input.map { it.split("").filter { it.isNotBlank() }.toMutableList() }.toMutableList()
    val r = Day25(grid)
    r.part1()
}

class Day25(private var grid: MutableList<MutableList<String>>) {
    private val EAST = ">"
    private val SOUTH = "v"
    private val BLANK = "."

    fun part1() {
        var steps = 0
        var movedEast = true
        var movedSouth = true
        while (movedEast || movedSouth) {
            steps++
            movedEast = moveEast()
            movedSouth = moveSouth()
        }
        println(steps)
    }

    private fun moveEast(): Boolean {
        var moved = false
        for (r in grid.indices) {
            var moveOG = false
            var c = 0
            while (c < grid[0].size) {
                if (grid[r][c] == EAST) {
                    if (c <= grid[0].size - 2 && grid[r][c + 1] == BLANK) {
                        moved = true
                        if (c == 0) moveOG = true
                        grid[r][c] = BLANK
                        grid[r][c + 1] = EAST
                        c++
                    } else if (c == grid[0].size - 1 && grid[r][0] == BLANK && !moveOG) {
                        moved = true
                        grid[r][c] = BLANK
                        grid[r][0] = EAST
                    }
                }
                c++
            }
        }
        return moved
    }

    private fun moveSouth(): Boolean {
        var moved = false
        for (c in grid[0].indices) {
            var moveOG = false
            var r = 0
            while (r < grid.size) {
                if (grid[r][c] == SOUTH) {
                    if (r <= grid.size - 2 && grid[r + 1][c] == BLANK) {
                        moved = true
                        if (r == 0) moveOG = true
                        grid[r][c] = BLANK
                        grid[r + 1][c] = SOUTH
                        r++
                    } else if (r == grid.size - 1 && grid[0][c] == BLANK && !moveOG) {
                        moved = true
                        grid[r][c] = BLANK
                        grid[0][c] = SOUTH
                    }
                }
                r++
            }
        }
        return moved
    }
}