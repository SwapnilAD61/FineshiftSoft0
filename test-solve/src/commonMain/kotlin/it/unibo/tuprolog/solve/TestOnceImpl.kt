package it.unibo.tuprolog.solve

import it.unibo.tuprolog.dsl.theory.prolog
import it.unibo.tuprolog.solve.exception.error.InstantiationError
import it.unibo.tuprolog.solve.exception.error.TypeError

internal class TestOnceImpl(
    private val solverFactory: SolverFactory,
    override val errorSignature: Signature
) : TestOnce {
    override fun testOnceCut() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = "once"("!")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                kotlin.collections.listOf(query.yes()),
                solutions
            )
        }
    }

    override fun testOnceCutOr() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = ("once"("!") and (("X" eq 1) or ("X" eq 2)))
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                with(query) {
                    kotlin.collections.listOf(
                        yes("X" to 1),
                        yes("X" to 2)
                    )
                },
                solutions
            )
        }
    }

    override fun testOnceRepeat() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = "once"(repeat)
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                kotlin.collections.listOf(query.yes()),
                solutions
            )
        }
    }

    override fun testOnceFail() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = "once"(fail)
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                kotlin.collections.listOf(query.no()),
                solutions
            )
        }
    }

    override fun testOnceNum() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = "once"(3)
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                kotlin.collections.listOf(
                    query.halt(
                        TypeError.forGoal(
                            DummyInstances.executionContext,
                            errorSignature,
                            TypeError.Expected.CALLABLE,
                            numOf(3)

                        )
                    )
                ),
                solutions
            )
        }
    }

    override fun testOnceX() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = "once"("X")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                kotlin.collections.listOf(
                    query.halt(
                        InstantiationError.forGoal(
                            DummyInstances.executionContext,
                            errorSignature,
                            varOf("X")
                        )
                    )
                ),
                solutions
            )
        }
    }
}
