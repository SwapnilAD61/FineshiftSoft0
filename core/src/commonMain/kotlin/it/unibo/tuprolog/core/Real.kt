package it.unibo.tuprolog.core

import org.gciatto.kt.math.BigDecimal
import org.gciatto.kt.math.BigInteger

interface Real : Numeric {

    override val isReal: Boolean
        get() = true

    val value: BigDecimal

    override val decimalValue: BigDecimal
        get() = value

    override val intValue: BigInteger
        get() = value.toBigInteger()

    companion object {

        fun of(real: BigDecimal): Real {
            return RealImpl(real)
        }

        fun of(real: Double): Real {
            return RealImpl(BigDecimal.of(real))
        }

        fun of(real: Float): Real {
            return RealImpl(BigDecimal.of(real))
        }

        fun of(real: String): Real {
            return RealImpl(BigDecimal.of(real))
        }
    }
}