package it.unibo.tuprolog.solve.problog.lib.rule

import it.unibo.tuprolog.core.Scope
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.solve.ExecutionContext
import it.unibo.tuprolog.solve.problog.lib.ProblogLib.PREDICATE_PREFIX
import it.unibo.tuprolog.solve.problog.lib.primitive.ProbExplAnd
import it.unibo.tuprolog.solve.problog.lib.primitive.ProbSolve
import it.unibo.tuprolog.solve.problog.lib.primitive.ProbSolveEvidence
import it.unibo.tuprolog.solve.rule.RuleWrapper
import kotlin.collections.List as KtList

internal object ProbSolveConditional : RuleWrapper<ExecutionContext>(
    "${PREDICATE_PREFIX}_solve_cond",
    3
) {

    override val Scope.head: KtList<Term>
        get() = ktListOf(varOf("QE"), varOf("E"), varOf("Q"))

    override val Scope.body: Term
        get() {
            val resultQ = varOf("RES_Q")
            val resultE = varOf("E")
            return tupleOf(
                structOf(ProbSolveEvidence.functor, resultE),
                atomOf("!"),
                structOf(ProbSolve.functor, resultQ, varOf("Q")),
                structOf(ProbExplAnd.functor, varOf("QE"), resultE, resultQ)
            )
        }
}
