package it.unibo.tuprolog.collections.impl

import it.unibo.tuprolog.collections.AbstractMutableReteClauseCollection
import it.unibo.tuprolog.collections.MutableClauseQueue
import it.unibo.tuprolog.collections.RetrieveResult
import it.unibo.tuprolog.collections.rete.custom.ReteTree
import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.theory.Theory

internal class MutableReteClauseQueue private constructor(
    private val rete: ReteTree
) : MutableClauseQueue, AbstractMutableReteClauseCollection<MutableReteClauseQueue>(rete) {

    /** Construct a [MutableReteClauseQueue] from given clauses */
    constructor(clauses: Iterable<Clause>) : this(ReteTree.ordered(clauses)) {
        Theory.checkClausesCorrect(clauses)
    }

    override fun getFirst(clause: Clause): Sequence<Clause> =
        rete.get(clause)

    override fun getLast(clause: Clause): Sequence<Clause> =
        getFirst(clause).toList().asReversed().asSequence()

    override fun addFirst(clause: Clause): MutableReteClauseQueue {
        rete.assertA(clause)
        return this
    }

    override fun addLast(clause: Clause): MutableReteClauseQueue {
        rete.assertZ(clause)
        return this
    }

    override fun retrieveFirst(clause: Clause): RetrieveResult<out MutableReteClauseQueue> {
        val retracted = rete.retractFirst(clause)

        return when {
            retracted.none() ->
                RetrieveResult.Failure(this)
            else ->
                RetrieveResult.Success(
                    this, retracted.toList()
                )
        }
    }
}