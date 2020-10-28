package it.unibo.tuprolog.solve.libs.io

import it.unibo.tuprolog.solve.libs.io.Url.Companion.ensureValidPort
import it.unibo.tuprolog.solve.libs.io.Url.Companion.str
import it.unibo.tuprolog.solve.libs.io.exceptions.InvalidUrlException
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import kotlin.streams.asSequence

class JvmUrl(private val url: URL) : Url {

    constructor(string: String) : this(string.toUrl())

    constructor(protocol: String, host: String = "", port: Int? = null, path: String = "", query: String? = null) :
        this("$protocol://$host${port?.ensureValidPort()?.str { ":$it" }}$path${query.str { "?$it" }}")

    override val protocol: String
        get() = url.protocol

    override val host: String
        get() = url.host

    override val path: String
        get() = url.path

    override val port: Int?
        get() = url.port.let { if (it > 0) it else null }

    override val query: String?
        get() = url.query

    override fun readAsText(): String =
        BufferedReader(InputStreamReader(url.openStream())).lines().asSequence().joinToString("\n")

    override fun readAsByteArray(): ByteArray =
        BufferedInputStream(url.openStream()).readAllBytes()

    override fun resolve(child: String): Url = JvmUrl(protocol, host, port, "$path/$child", query)

    override fun toString(): String = url.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Url) return false

        if (toString() != other.toString()) return false

        return true
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    companion object {
        private fun String.toUrl(): URL =
            try {
                URL(this)
            } catch (e: MalformedURLException) {
                throw InvalidUrlException(
                    message = "Invalid URL: $this",
                    cause = e
                )
            }
    }
}