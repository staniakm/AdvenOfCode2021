import kotlin.math.max

fun main() {
    val calculateOverlapsVents = day5SampleData()
        .map { mapToPoints(it) }
        .filter { it.pointsInLine() }
        .onEach { it.drawLine() }
        .let {
            Map().convertToMap(it)
        }.calculateOverlapsVents()
    println("number of overlap vents:  $calculateOverlapsVents")
    check(calculateOverlapsVents == 6113)

    //part 2
    val calculateOverlapsVents1 = day5SampleData()
        .map { mapToPoints(it) }
        .onEach { it.drawLineWithDiagonal() }
        .let {
            Map().convertToMap(it)
        }.calculateOverlapsVents()
    println("Number of overlaps vents: $calculateOverlapsVents1")
    check(calculateOverlapsVents1 == 20373)
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
        map[it] = map.getOrDefault(it, 0) + 1
    }

    fun calculateOverlapsVents(): Int {
        return (0..y)
            .sumOf { h ->
                (0..x)
                    .map { map.getOrDefault(Point(it, h), 0) }
                    .count { it > 1 }
            }
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
    private val pointsBetween = mutableListOf<Point>()

    fun pointsInLine(): Boolean {
        return startPoint.isInLineWith(endPoint)
    }

    fun drawLine() {
        draw(false).let { pointsBetween.addAll(it) }
    }

    fun drawLineWithDiagonal() {
        draw(true).let { pointsBetween.addAll(it) }
    }

    private fun draw(withDiagonal: Boolean): List<Point> {
        return if (startPoint.x == endPoint.x) {
            drawHorizontalLine()
        } else if (startPoint.y == endPoint.y) {
            drawVerticalLine()
        } else {
            if (withDiagonal) drawDiagonalLine() else listOf()
        }
    }

    private fun drawDiagonalLine(): List<Point> {
        val startPointX: Point = if (startPoint.x < endPoint.x) startPoint else endPoint
        val endpoint = if (startPoint.x > endPoint.x) startPoint else endPoint
        val progression = if (startPointX.y > endpoint.y) -1 else 1
        return (0..(endpoint.x - startPointX.x))
            .map {
                Point(startPointX.x + it, startPointX.y + (it * progression))
            }
    }

    private fun drawHorizontalLine(): List<Point> {
        val start = if (endPoint.y > startPoint.y) startPoint.y else endPoint.y
        val endPoint = if (endPoint.y < startPoint.y) startPoint.y else endPoint.y
        return (start..endPoint)
            .map { Point(startPoint.x, it) }
    }

    private fun drawVerticalLine(): List<Point> {
        val start = if (endPoint.x > startPoint.x) startPoint.x else endPoint.x
        val endPoint = if (endPoint.x < startPoint.x) startPoint.x else endPoint.x
        return (start..endPoint)
            .map { Point(it, startPoint.y) }

    }

    fun getGreatestX(): Int {
        return max(startPoint.x, endPoint.x)
    }

    fun getGreatestY(): Int {
        return max(startPoint.y, endPoint.y)
    }

    fun getLine(): List<Point> {
        return pointsBetween
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

