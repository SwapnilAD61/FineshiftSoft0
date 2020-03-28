package it.unibo.tuprolog.solve.library.stdlib.primitive

import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.solve.primitive.PrimitiveWrapper
import it.unibo.tuprolog.solve.Solution
import it.unibo.tuprolog.solve.Solve
import it.unibo.tuprolog.solve.StreamsSolver
import it.unibo.tuprolog.solve.solver.ExecutionContextImpl
import it.unibo.tuprolog.solve.solver.newSolveRequest
import it.unibo.tuprolog.solve.solver.replyWith

/**
 * Implementation of primitive handling `'\+'/1` behaviour
 *
 * @author Enrico
 */
internal object Not : PrimitiveWrapper<ExecutionContextImpl>("\\+", 1) {

    override fun uncheckedImplementation(request: Solve.Request<ExecutionContextImpl>): Sequence<Solve.Response> =
        sequence {
            val goalArgument = request.arguments.single()

            StreamsSolver.solveToResponses(request.newSolveRequest(Struct.of(Call.functor, goalArgument))).forEach { goalResponse ->
                when (goalResponse.solution) {
                    is Solution.Yes -> {
                        yield(request.replyFail())
                        return@sequence
                    }

                    is Solution.No -> yield(request.replySuccess(request.context.substitution))

                    else -> yield(request.replyWith(goalResponse))
                }
            }
        }
}
