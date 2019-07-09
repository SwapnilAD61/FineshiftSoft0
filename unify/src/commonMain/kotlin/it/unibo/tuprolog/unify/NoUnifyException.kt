package it.unibo.tuprolog.unify

import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.TuprologRuntimeException

open class NoUnifyException(private val term1: Term, private val term2: Term, other: Throwable?) : TuprologRuntimeException(other) {

    constructor(term1: Term, term2: Term) : this(term1, term2, null)

    override val message: String?
        get() = "Cannot match term `$term1` with term `$term2`"
}

