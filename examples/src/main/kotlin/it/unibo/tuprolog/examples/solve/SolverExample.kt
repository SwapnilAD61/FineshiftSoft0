package it.unibo.tuprolog.examples.solve

import it.unibo.tuprolog.core.Numeric
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.dsl.theory.prolog
import it.unibo.tuprolog.solve.*
import it.unibo.tuprolog.solve.exception.error.TypeError
import it.unibo.tuprolog.solve.library.Libraries
import it.unibo.tuprolog.solve.library.Library

val gtSignature = Signature("gt", 2)

fun gt(request: Solve.Request<ExecutionContext>): Sequence<Solve.Response> {
    val arg1: Term = request.arguments[0]
    val arg2: Term = request.arguments[1]

    if (arg1 !is Numeric) {
        throw TypeError.forGoal(
            request.context,
            request.signature,
            TypeError.Expected.NUMBER,
            arg1
        )
    }
    if (arg2 !is Numeric) {
        throw TypeError.forGoal(
            request.context,
            request.signature,
            TypeError.Expected.NUMBER,
            arg2
        )
    }

    return if (arg1.castTo<Numeric>().decimalValue > arg2.castTo<Numeric>().decimalValue) {
        sequenceOf(request.replySuccess())
    } else {
        sequenceOf(request.replyFail())
    }
}

fun main() {
    prolog {
        val solver = Solver.classicWithDefaultBuiltins(
            libraries = Libraries(
                Library.of(
                    primitives = mapOf(gtSignature to ::gt),
                    alias = "it.unibo.lrizzato.myprimives"
                )
            ),
            staticKb = theoryOf(
                fact { "user"("giovanni") },
                fact { "user"("lorenzo") },
                rule { "user"(`_`) impliedBy fail() }
            )
        )
        val query = "user"("X") and "write"("hello: ") and "write"("X") and "nl" and "gt"(2, 1)
        solver.solve(query).forEach {
            when (it) {
                is Solution.No -> println("no.\n")
                is Solution.Yes -> {
                    println("yes: ${it.solvedQuery}")
                    for (assignment in it.substitution) {
                        println("\t${assignment.key} / ${assignment.value}")
                    }
                    println()
                }
                is Solution.Halt -> {
                    println("halt: ${it.exception.message}")
                    for (err in it.exception.prologStackTrace) {
                        println("\t $err")
                    }
                }
            }
        }
    }
}