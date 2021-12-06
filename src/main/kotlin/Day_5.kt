import kotlin.math.abs

fun main() {
    val calculateOverlapsVents = day5SampleData()
        .map { mapToCoordinate(it) }
        .filter { it.pointsInLine() }
        .let {
            calculateOverlapSum(it)
        }
    println("number of overlap vents:  $calculateOverlapsVents")
    check(calculateOverlapsVents == 6113)

    //part 2
    val calculateOverlapsVents1 = day5SampleData()
        .map { mapToCoordinate(it) }
        .let {
            calculateOverlapSum(it)
        }
    println("Number of overlaps vents: $calculateOverlapsVents1")
    check(calculateOverlapsVents1 == 20373)
}

fun calculateOverlapSum(coordinates: List<Coordinates>): Int {
    return coordinates.map { it.getLine() }
        .flatten()
        .groupingBy { it }.eachCount()
        .filter { it.value > 1 }.count()
}

fun mapToCoordinate(input: String): Coordinates {
    return input.split("->").map { coordinate ->
        coordinate.trim().split(",").let {
            Point(it[0].toInt(), it[1].toInt())
        }
    }.let {
        Coordinates(it[0], it[1])
    }
}

data class Coordinates(val startPoint: Point, val endPoint: Point) {
    private val pointsBetween: List<Point>

    init {
        pointsBetween = calculatePointsBetween()
    }

    fun pointsInLine(): Boolean {
        return startPoint.isInLineWith(endPoint)
    }

    private fun calculatePointsBetween(): List<Point> {
        return if (startPoint.x == endPoint.x) {
            drawHorizontalLine()
        } else if (startPoint.y == endPoint.y) {
            drawVerticalLine()
        } else {
            drawDiagonalLine()
        }
    }

    private fun drawDiagonalLine(): List<Point> {
        val sPoint: Point = if (startPoint.x < endPoint.x) startPoint else endPoint
        val ePoint = if (startPoint.x > endPoint.x) startPoint else endPoint
        val progression = if (sPoint.y > ePoint.y) -1 else 1
        return (0..(ePoint.x - sPoint.x))
            .map {
                Point(sPoint.x + it, sPoint.y + (it * progression))
            }
    }

    private fun drawHorizontalLine(): List<Point> {
        val progression = if (startPoint.y > endPoint.y) -1 else 1
        return (0..abs(startPoint.y - endPoint.y))
            .map { Point(startPoint.x, startPoint.y + (it * progression)) }
    }

    private fun drawVerticalLine(): List<Point> {
        val progression = if (startPoint.x > endPoint.x) -1 else 1
        return (0..abs(startPoint.x - endPoint.x))
            .map { Point(startPoint.x + (it * progression), startPoint.y) }

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

