
package com.saigeethika.analyzer

import com.saigeethika.analyzer.exceptions.EmptyContentException

/**
 * Contains core business logic.
 * Designed to be easy to reason about and test.
 */
object TextProcessor {

  def findTopWords(text: String, limit: Int): List[(String, Int)] = {

    require(limit > 0, "Limit must be greater than zero")

    if (text == null || text.trim.isEmpty)
      throw new EmptyContentException("Text content is empty")

    val words = text
      .toLowerCase
      .replaceAll("[^a-z]", " ")
      .split("\\s+")
      .iterator
      .filter(word =>
        word.nonEmpty && !StopWords.common.contains(word)
      )

    words
      .toSeq
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toList
      .sortBy(-_._2)
      .take(limit)
  }
}
