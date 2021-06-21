package it.unibo.tuprolog.collections.rete.custom.leaf

import it.unibo.tuprolog.collections.rete.custom.Retractable
import it.unibo.tuprolog.collections.rete.custom.Utils
import it.unibo.tuprolog.collections.rete.custom.Utils.nestedFirstArgument
import it.unibo.tuprolog.collections.rete.custom.clause.IndexedClause
import it.unibo.tuprolog.collections.rete.custom.clause.SituatedIndexedClause
import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.unify.Unificator.Companion.matches
import it.unibo.tuprolog.utils.addFirst
import it.unibo.tuprolog.utils.buffered
import it.unibo.tuprolog.utils.dequeOf

internal class AtomIndex(
    private val ordered: Boolean,
    private val nestingLevel: Int
) : AbstractIndexingLeaf(), Retractable {

    private val index: MutableMap<Atom, MutableList<SituatedIndexedClause>> = mutableMapOf()

    override fun get(clause: Clause): Sequence<Clause> =
        if (clause.nestedFirstArgument().isAtom) {
            index[clause.asInnerAtom()]
                ?.asSequence()
                ?.filter { it.innerClause matches clause }
                ?.map { it.innerClause }
                ?: emptySequence()
        } else {
            extractGlobalSequence(clause)
        }

    override fun assertA(clause: IndexedClause) {
        if (ordered) {
            clause.asInnerAtom().let {
                index.getOrPut(it) { dequeOf() }.addFirst(SituatedIndexedClause.of(clause + this, this))
            }
        } else {
            assertZ(clause)
        }
    }

    override fun assertZ(clause: IndexedClause) {
        clause.asInnerAtom().let {
            index.getOrPut(it) { dequeOf() }.add(SituatedIndexedClause.of(clause + this, this))
        }
    }

    override fun getFirstIndexed(clause: Clause): SituatedIndexedClause? =
        if (clause.nestedFirstArgument().isAtom) {
            index[clause.asInnerAtom()].let {
                if (it == null) {
                    null
                } else {
                    extractFirst(clause, it)
                }
            }
        } else {
            extractFirst(clause)
        }

    private fun extractFirst(clause: Clause): SituatedIndexedClause? =
        index.values.mapNotNull {
            extractFirst(clause, it)
        }.minOrNull()

    private fun extractFirst(clause: Clause, index: MutableList<SituatedIndexedClause>): SituatedIndexedClause? {
        val actualIndex = index.indexOfFirst { it.innerClause matches clause }

        return if (actualIndex == -1) null
        else index[actualIndex]
    }

    override fun getIndexed(clause: Clause): Sequence<SituatedIndexedClause> {
        return if (clause.nestedFirstArgument().isAtom) {
            index[clause.asInnerAtom()]
                ?.asSequence()
                ?.filter { it.innerClause matches clause }
                ?: emptySequence()
        } else {
            extractGlobalIndexedSequence(clause)
        }
    }

    override fun retractIndexed(indexed: SituatedIndexedClause) {
        index[indexed.asInnerAtom()]!!.remove(indexed)
    }

    override fun retractAllIndexed(clause: Clause): Sequence<SituatedIndexedClause> {
        return if (clause.nestedFirstArgument().isAtom) {
            when (val partialIndex = index[clause.asInnerAtom()]) {
                null -> emptySequence()
                else -> Utils.removeAllLazily(partialIndex, clause)
            }
        } else {
            Utils.merge(
                index.values.asSequence().map {
                    Utils.removeAllLazily(it, clause)
                }
            )
        }.buffered()
    }

    override fun retractAll(clause: Clause): Sequence<Clause> =
        retractAllIndexed(clause).map { it.innerClause }

    override fun getCache(): Sequence<SituatedIndexedClause> =
        Utils.merge(index.values.asSequence().map { it.asSequence() })

    override fun extractGlobalIndexedSequence(clause: Clause): Sequence<SituatedIndexedClause> =
        getCache().filter { it.innerClause matches clause }

    private fun extractGlobalSequence(clause: Clause): Sequence<Clause> =
        extractGlobalIndexedSequence(clause).map { it.innerClause }

    private fun Clause.nestedFirstArgument(): Term =
        this.head!!.nestedFirstArgument(nestingLevel + 1)

    private fun Clause.asInnerAtom(): Atom =
        this.nestedFirstArgument().castToAtom()

    private fun SituatedIndexedClause.asInnerAtom(): Atom =
        this.innerClause.nestedFirstArgument().castToAtom()

    private fun IndexedClause.asInnerAtom(): Atom =
        this.innerClause.nestedFirstArgument().castToAtom()
}
