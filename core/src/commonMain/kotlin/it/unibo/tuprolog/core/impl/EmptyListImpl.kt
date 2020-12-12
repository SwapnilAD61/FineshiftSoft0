package it.unibo.tuprolog.core.impl

import it.unibo.tuprolog.core.Empty
import it.unibo.tuprolog.core.EmptyList
import it.unibo.tuprolog.core.Term

internal class EmptyListImpl(
    tags: Map<String, Any> = emptyMap()
) : AtomImpl(Empty.EMPTY_LIST_FUNCTOR, tags), EmptyList {

    override val unfoldedList: List<Term> = listOf(this)

    override val unfoldedSequence: Sequence<Term> = sequenceOf(this)

    override val unfoldedArray: Array<Term> = arrayOf(this)

    override fun unfold(): Sequence<Term> = sequenceOf(this)

    override fun toString(): String = value

    override val last: Term
        get() = this

    override fun replaceTags(tags: Map<String, Any>): EmptyList {
        return EmptyListImpl(tags)
    }
}
