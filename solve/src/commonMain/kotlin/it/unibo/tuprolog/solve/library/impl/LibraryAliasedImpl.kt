package it.unibo.tuprolog.solve.library.impl

import it.unibo.tuprolog.core.operators.OperatorSet
import it.unibo.tuprolog.solve.Signature
import it.unibo.tuprolog.solve.function.LogicFunction
import it.unibo.tuprolog.solve.library.AliasedLibrary
import it.unibo.tuprolog.solve.primitive.Primitive
import it.unibo.tuprolog.theory.Theory

/**
 * Default implementation class of [AliasedLibrary]
 *
 * @author Enrico
 */
open class LibraryAliasedImpl(
    override val operators: OperatorSet,
    override val theory: Theory,
    override val primitives: Map<Signature, Primitive>,
    override val functions: Map<Signature, LogicFunction>,
    override val alias: String
) : LibraryImpl(operators, theory, primitives, functions), AliasedLibrary {

    override fun toString(): String =
        "Library(alias='$alias', operators=$operators, theory=$theory, primitives=$primitives, functions=$functions)"
}
