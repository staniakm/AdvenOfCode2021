import java.io.File

fun readFileFromResources(fileName:String) = File("src/main/resources/$fileName").readText().lines().filter { it.isNotBlank() }
