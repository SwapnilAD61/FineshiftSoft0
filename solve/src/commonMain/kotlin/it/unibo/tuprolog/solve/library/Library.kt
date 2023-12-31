package it.unibo.tuprolog.solve.library

import it.unibo.tuprolog.core.operators.Operator
import it.unibo.tuprolog.core.operators.OperatorSet
import it.unibo.tuprolog.solve.Signature
import it.unibo.tuprolog.solve.function.LogicFunction
import it.unibo.tuprolog.solve.library.impl.LibraryAliasedImpl
import it.unibo.tuprolog.solve.library.impl.LibraryImpl
import it.unibo.tuprolog.solve.primitive.Primitive
import it.unibo.tuprolog.theory.Theory
import kotlin.js.JsName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/** Represents a Prolog library */
interface Library {

    /** Library defined operators */
    @JsName("operators")
    val operators: OperatorSet

    /** The library theory clauses */
    @JsName("theory")
    val theory: Theory

    /** The library primitives, identified by their signatures */
    @JsName("primitives")
    val primitives: Map<Signature, Primitive>

    /** The library prolog functions, identified by their signature */
    @JsName("functions")
    val functions: Map<Signature, LogicFunction>

    /**
     * Checks whether this library contains the provided signature.
     *
     * The default implementation, checks for signature presence among primitives and theory clauses by indicator-like search
     */
    @JsName("containsSignature")
    operator fun contains(signature: Signature): Boolean =
        primitives.containsKey(signature) ||
            signature.toIndicator().let { theory.contains(it) }

    /** Checks whether this library contains the definition of provided operator */
    @JsName("containsOperator")
    operator fun contains(operator: Operator): Boolean = operator in operators

    /** Checks whether this library has a [Primitive] with provided signature */
    @JsName("hasPrimitive")
    fun hasPrimitive(signature: Signature): Boolean = signature in primitives.keys

    /** Checks whether the provided signature, is protected in this library */
    @JsName("hasProtected")
    fun hasProtected(signature: Signature): Boolean = signature in this

    companion object {

        @JvmStatic
        @JsName("sequenceToMapEnsuringNoDuplicates")
        fun <T> Sequence<Pair<Signature, T>>.toMapEnsuringNoDuplicates(): Map<Signature, T> {
            val result = mutableMapOf<Signature, T>()
            for ((signature, value) in this) {
                if (result.containsKey(signature)) {
                    throw IllegalArgumentException("Repeated entry: $signature")
                }
                result[signature] = value
            }
            return result
        }

        @JvmStatic
        @JsName("iterableToMapEnsuringNoDuplicates")
        fun <T> Iterable<Pair<Signature, T>>.toMapEnsuringNoDuplicates(): Map<Signature, T> =
            asSequence().toMapEnsuringNoDuplicates()

        /** Creates an instance of [Library] with given parameters */
        @JvmStatic
        @JsName("unaliased")
        @JvmOverloads
        fun unaliased(
            primitives: Map<Signature, Primitive> = emptyMap(),
            theory: Theory = Theory.empty(),
            operatorSet: OperatorSet = OperatorSet(),
            functions: Map<Signature, LogicFunction> = emptyMap()
        ): Library = LibraryImpl(operatorSet, theory, primitives, functions)

        /** Creates an instance of [AliasedLibrary] with given parameters */
        @JvmStatic
        @JsName("aliased")
        @JvmOverloads
        fun aliased(
            alias: String,
            primitives: Map<Signature, Primitive> = emptyMap(),
            theory: Theory = Theory.empty(),
            operatorSet: OperatorSet = OperatorSet(),
            functions: Map<Signature, LogicFunction> = emptyMap()
        ): AliasedLibrary = LibraryAliasedImpl(operatorSet, theory, primitives, functions, alias)

        /** Creates an instance of [AliasedLibrary] starting from [Library] and an alias */
        @JvmStatic
        @JsName("of")
        fun of(alias: String, library: Library): AliasedLibrary =
            LibraryAliasedImpl(library.operators, library.theory, library.primitives, library.functions, alias)
    }
}
