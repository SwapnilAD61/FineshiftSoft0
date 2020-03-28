@file:JvmName("Extensions")
package it.unibo.tuprolog.solve

import it.unibo.tuprolog.core.Scope
import it.unibo.tuprolog.core.Struct
import kotlin.jvm.JvmName

/** Extracts this [Struct] indicator and converts it to [Signature] */
fun Struct.extractSignature(): Signature =
    Signature.fromIndicator(indicator)!!

fun Solver.solve(
    maxDuration: TimeDuration = TimeDuration.MAX_VALUE,
    scopedContext: Scope.() -> Struct
): Sequence<Solution> = solve(scopedContext(Scope.empty()), maxDuration)

