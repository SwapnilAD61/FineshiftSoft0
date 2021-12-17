package it.unibo.tuprolog.solve

import it.unibo.tuprolog.dsl.theory.prolog

class TestSetOfImpl(private val solverFactory: SolverFactory) : TestSetOf {

    override fun testSetOfBasic() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = setof("X", ("X" eq 1) or ("X" eq 2), "S")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                ktListOf(query.yes("S" to listOf(1, 2))),
                solutions
            )
        }
    }

    override fun testSetOfX() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = setof("X", ("X" eq 1) or ("X" eq 2), "X")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                ktListOf(query.yes("X" to listOf(1, 2))),
                solutions
            )
        }
    }

    override fun testSetOfNoSorted() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = setof("X", ("X" eq 2) or ("X" eq 1), "X")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                ktListOf(query.yes("X" to listOf(1, 2))),
                solutions
            )
        }
    }

    override fun testSetOfDoubled() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = setof("X", ("X" eq 2) or ("X" eq 2), "L")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                ktListOf(query.yes("L" to listOf(2))),
                solutions
            )
        }
    }

    override fun testSetOfFail() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = bagof("X", fail, "L")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                ktListOf(query.no()),
                solutions
            )
        }
    }

    override fun testSetOfAsFindAll() {
        prolog {
            val solver = solverFactory.solverWithDefaultBuiltins()

            val query = setof("X", "Y" sup ((("X" eq 1) or ("Y" eq 1)) or (("X" eq 2) or ("Y" eq 2))), "S")
            val solutions = solver.solve(query, mediumDuration).toList()

            assertSolutionEquals(
                ktListOf(query.yes("S" to listOf(1, 2))),
                solutions
            )
        }
    }
}
