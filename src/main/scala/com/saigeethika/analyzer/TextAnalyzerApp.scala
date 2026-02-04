package com.saigeethika.analyzer

/**
 * Author: Sai Geethika
 *
 * Entry point of the application.
 * Keeps orchestration simple and readable for interviews.
 */
object TextAnalyzerApp {

  def main(args: Array[String]): Unit = {
    // Java version check
    val javaVersion = System.getProperty("java.version")
    val majorVersion = try {
      val parts = javaVersion.split("[.]")
      if (parts(0) == "1" && parts.length > 1) parts(1).toInt
      else parts(0).toInt
    } catch {
      case _: Throwable => 0
    }
    if (majorVersion < 17) {
      println(s"ERROR: Java 17+ is required. Detected version: $javaVersion")
      System.exit(1)
    }

    val bookUrl =
      if (args.nonEmpty) args(0)
      else "http://www.gutenberg.org/files/2701/2701-0.txt"

    try {
      println("Starting text analysis...")

      val content = FileLoader.load(bookUrl)
      val topWords = TextProcessor.findTopWords(content, 50)

      println("\nTop 50 most frequent words:\n")
      topWords.foreach {
        case (word, count) =>
          println(f"$word%-15s -> $count")
      }

      println("\nText analysis completed successfully.")

    } catch {
      case ex: Exception =>
        println(s"Application failed: ${ex.getMessage}")
    }
  }
}
