
package com.saigeethika.analyzer

import org.scalatest.funsuite.AnyFunSuite

class TextAnalyzerTest extends AnyFunSuite {

  test("Valid text should return word frequencies") {
    val text = "Scala is fast and Scala is expressive"
    val result = TextProcessor.findTopWords(text, 5)
    assert(result.contains(("scala", 2)))
  }

  test("Empty text should throw exception") {
    assertThrows[Exception] {
      TextProcessor.findTopWords("", 10)
    }
  }

  test("Invalid limit should fail fast") {
    assertThrows[IllegalArgumentException] {
      TextProcessor.findTopWords("Scala", 0)
    }
  }
}
