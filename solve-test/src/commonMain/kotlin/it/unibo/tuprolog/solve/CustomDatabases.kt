package it.unibo.tuprolog.solve

import it.unibo.tuprolog.dsl.theory.prolog
import it.unibo.tuprolog.theory.ClauseDatabase
import kotlin.collections.listOf as ktListOf

object CustomDatabases {

    val ifThenDatabase1: ClauseDatabase by lazy {
        prolog {
            theoryOf(
                fact { "a"(1) },
                fact { "a"(2) },
                fact { "b"(1) },
                fact { "c"(2) }
            )
        }
    }

    val ifThenDatabase2: ClauseDatabase by lazy {
        prolog {
            theoryOf(
                fact { "a"(2) },
                fact { "a"(1) },
                fact { "b"(1) },
                fact { "c"(2) }
            )
        }
    }

    val ifThen1ToSolution by lazy {
        prolog {
            ktListOf(
                ("a"("X") then "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"("X") then "c"("X")).hasSolutions(
                    { no() }
                ),
                ("a"(1) then "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"(2) then "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"(1) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"(2) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                (("a"("X")  and "!") then "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                (("a"("X")  and "!") then "c"("X")).hasSolutions(
                    { no() }
                ),
                (("a"("X")  and ("X" greaterThan 1)) then "b"("X")).hasSolutions(
                    { no() }
                ),
                (("a"("X")  and ("X" greaterThan 1)) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                (("a"("X")  and "!" and ("X" greaterThan 1)) then "b"("X")).hasSolutions(
                    { no() }
                ),
                (("a"("X")  and "!" and ("X" greaterThan 1)) then "c"("X")).hasSolutions(
                    { no() }
                )
            )
        }
    }

    val ifThen2ToSolution by lazy {
        prolog {
            ktListOf(
                ("a"("X") then "b"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"(1) then "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"(2) then "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"(1) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"(2) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                (("a"("X")  and "!") then "b"("X")).hasSolutions(
                    { no() }
                ),
                (("a"("X")  and "!") then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                (("a"("X")  and ("X" greaterThan 1)) then "b"("X")).hasSolutions(
                    { no() }
                ),
                (("a"("X")  and ("X" greaterThan 1)) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                (("a"("X")  and "!" and ("X" greaterThan 1)) then "b"("X")).hasSolutions(
                    { no() }
                ),
                (("a"("X")  and "!" and ("X" greaterThan 1)) then "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                )
            )
        }
    }

    val ifThenElse1ToSolution by lazy {
        prolog {
            ktListOf(
                ("a"("X") then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"("X") and ("X" greaterThan 1) then "b"("X") or "c"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and ("X" lowerThan 2) then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"("X") and "!" then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"("X") and "!" and ("X" greaterThan 1) then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"("X") and "!" and ("X" lowerThan 2) then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),

                ("a"("X") then "c"("X") or "b"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and ("X" greaterThan 1) then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"("X") and ("X" lowerThan 2) then "c"("X") or "b"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and "!" then "c"("X") or "b"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and "!" and ("X" greaterThan 1) then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"("X") and "!" and ("X" lowerThan 2) then "c"("X") or "b"("X")).hasSolutions(
                    { no() }
                )
            )
        }
    }

    val ifThenElse2ToSolution by lazy {
        prolog {
            ktListOf(
                ("a"("X") then "b"("X") or "c"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and ("X" greaterThan 1) then "b"("X") or "c"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and ("X" lowerThan 2) then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 1) }
                ),
                ("a"("X") and "!" then "b"("X") or "c"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and "!" and ("X" greaterThan 1) then "b"("X") or "c"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and "!" and ("X" lowerThan 2) then "b"("X") or "c"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),

                ("a"("X") then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"("X") and ("X" greaterThan 1) then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"("X") and ("X" lowerThan 2) then "c"("X") or "b"("X")).hasSolutions(
                    { no() }
                ),
                ("a"("X") and "!" then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"("X") and "!" and ("X" greaterThan 1) then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 2) }
                ),
                ("a"("X") and "!" and ("X" lowerThan 2) then "c"("X") or "b"("X")).hasSolutions(
                    { yes("X" to 1) }
                )
            )
        }
    }
}