
package com.saigeethika.analyzer

import scala.io.Source
import com.saigeethika.analyzer.exceptions.InvalidUrlException

/**
 * Responsible only for loading external content.
 */
object FileLoader {

  def load(url: String): String = {

    if (url == null || url.trim.isEmpty)
      throw new InvalidUrlException("URL cannot be empty")

    if (!url.startsWith("http"))
      throw new InvalidUrlException("Only HTTP/HTTPS URLs are supported")

    val source = Source.fromURL(url)
    try {
      val content = source.mkString
      if (content.trim.isEmpty)
        throw new RuntimeException("Downloaded content is empty")
      content
    } finally {
      source.close()
    }
  }
}
