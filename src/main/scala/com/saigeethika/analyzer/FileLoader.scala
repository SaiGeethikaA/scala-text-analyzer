
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

    import java.net.{URL, HttpURLConnection}
    import java.io.{BufferedReader, InputStreamReader}

    def getConnection(url: String, maxRedirects: Int = 5): HttpURLConnection = {
      var currentUrl = url
      var redirects = 0
      while (redirects < maxRedirects) {
        val conn = new URL(currentUrl).openConnection().asInstanceOf[HttpURLConnection]
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; TextAnalyzer/1.0)")
        conn.setConnectTimeout(10000)
        conn.setReadTimeout(10000)
        conn.setInstanceFollowRedirects(false)
        val code = conn.getResponseCode
        if (code == 301 || code == 302 || code == 303 || code == 307 || code == 308) {
          val location = conn.getHeaderField("Location")
          if (location == null) throw new RuntimeException("Redirect with no Location header")
          currentUrl = location
          redirects += 1
          conn.disconnect()
        } else {
          return conn
        }
      }
      throw new RuntimeException("Too many redirects")
    }

    val connection = getConnection(url)
    val responseCode = connection.getResponseCode
    println(s"HTTP response code: $responseCode")
    val headers = connection.getHeaderFields
    println("Response headers:")
    import scala.jdk.CollectionConverters._
    headers.asScala.foreach { case (k, v) => println(s"  $k: ${v.asScala.mkString(", ")}") }

    val reader = new BufferedReader(new InputStreamReader(connection.getInputStream))
    val content = new StringBuilder
    try {
      var line = reader.readLine()
      while (line != null) {
        content.append(line).append("\n")
        line = reader.readLine()
      }
      val result = content.toString
      if (result.trim.isEmpty)
        throw new RuntimeException("Downloaded content is empty")
      result
    } finally {
      reader.close()
      connection.disconnect()
    }
  }
}
