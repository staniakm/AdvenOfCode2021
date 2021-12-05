import kotlin.math.max

fun main() {
    day5SampleData()
        .map { mapToPoints(it) }
        .filter { it.pointsInLine() }
        .onEach { it.drawLine() }
        .let {
            Map().convertToMap(it)
        }.drawMap()


    //part 2
    day5SampleData()
        .map { mapToPoints(it) }
        .onEach { it.drawLineWithDiagonal() }
        .let {
            Map().convertToMap(it)
        }.drawMap()
}

class Map() {
    private var x: Int = 0
    private var y: Int = 0
    private val map: MutableMap<Point, Int> = mutableMapOf()
    fun convertToMap(coordinates: List<Coordinates>): Map {
        x = coordinates.maxOf { it.getGreatestX() }
        y = coordinates.maxOf { it.getGreatestY() }
        coordinates.map {
            it.getLine()
        }.forEach {
            addLine(it)
        }
        return this
    }

    private fun addLine(line: List<Point>) {
        line.forEach {
            addPoint(it)
        }

    }

    private fun addPoint(it: Point) {
        val value = map.getOrDefault(it, 0)
        map[it] = value + 1
    }

    fun drawMap() {
        var countOverlapVents = 0
        for (h in 0..y) {
            for (v in 0..x) {
                val nrOfVents = map.getOrDefault(Point(v, h), 0)
                if (nrOfVents > 1) {
                    countOverlapVents++
                }
            }
        }
        println("\nNumber of overlap vents: $countOverlapVents")
    }

}

fun mapToPoints(input: String): Coordinates {
    return input.split("->").map { coordinate ->
        coordinate.trim().split(",").let {
            Point(it[0].toInt(), it[1].toInt())
        }
    }.let {
        Coordinates(it[0], it[1])
    }
}

data class Coordinates(val startPoint: Point, val endPoint: Point) {
    private val line = mutableListOf<Point>()

    fun pointsInLine(): Boolean {
        return startPoint.isInLineWith(endPoint)
    }

    fun drawLine() {
        if (startPoint.x == endPoint.x) {
            drawHorizontalLine()
        } else if (startPoint.y == endPoint.y) {
            drawVerticalLine()
        }
    }

    fun drawLineWithDiagonal() {
        if (startPoint.x == endPoint.x) {
            drawHorizontalLine()
        } else if (startPoint.y == endPoint.y) {
            drawVerticalLine()
        } else {
            drawDiagonalLine()
        }
    }

    private fun drawDiagonalLine() {
        val startPointX: Point = if (startPoint.x < endPoint.x) startPoint else endPoint
        val endpoint = if (startPoint.x > endPoint.x) startPoint else endPoint
        val progression = if (startPointX.y > endpoint.y) -1 else 1
        line.add(Point(startPointX.x, startPointX.y))
        var counter = 1
        while (!line.contains(endpoint)) {
            line.add(Point(startPointX.x + counter, startPointX.y +  (counter * progression)))
            counter++
        }
    }

    private fun drawHorizontalLine() {
        val start = if (endPoint.y > startPoint.y) startPoint.y else endPoint.y
        val endPoint = if (endPoint.y < startPoint.y) startPoint.y else endPoint.y
        for (y in start..endPoint) {
            line.add(Point(startPoint.x, y))
        }
    }

    private fun drawVerticalLine() {
        val start = if (endPoint.x > startPoint.x) startPoint.x else endPoint.x
        val endPoint = if (endPoint.x < startPoint.x) startPoint.x else endPoint.x
        for (x in start..endPoint) {
            line.add(Point(x, startPoint.y))
        }
    }

    fun getGreatestX(): Int {
        return max(startPoint.x, endPoint.x)
    }

    fun getGreatestY(): Int {
        return max(startPoint.y, endPoint.y)
    }

    fun getLine(): List<Point> {
        return line
    }
}

data class Point(val x: Int, val y: Int) {
    fun isInLineWith(point: Point): Boolean {
        return x == point.x || y == point.y
    }
}

fun day5SampleData() = readFileFromResources("day5_data")

fun sampleData5() = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2""".lines().filter { it.isNotBlank() }

