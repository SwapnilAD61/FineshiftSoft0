package it.unibo.tuprolog.solve.stdlib.primitive

import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.solve.ExecutionContext
import it.unibo.tuprolog.solve.Solve
import it.unibo.tuprolog.solve.exception.TuPrologRuntimeException

abstract class TypeEnsurer<E : ExecutionContext>(typeName: String) : UnaryPredicate<E>(typeName) {
    override fun uncheckedImplementation(request: Solve.Request<E>): Sequence<Solve.Response> = sequence {
        with (request) {
            ensureType(context, arguments[0][context.substitution])
            yield(replySuccess())
        }
    }

    abstract fun ensureType(context: E, term: Term)

}