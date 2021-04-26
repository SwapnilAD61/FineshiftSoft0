package it.unibo.tuprolog.bdd

import it.unibo.tuprolog.bdd.impl.builder.ReducedBinaryDecisionDiagramBuilder
import it.unibo.tuprolog.bdd.impl.builder.SimpleBinaryDecisionDiagramBuilder
import kotlin.js.JsName

/**
 * This interfaces hides the strategy with which instances of
 * [BinaryDecisionDiagram] are created. Platform-specific optimized
 * representations of BDDs can be introduced by providing new
 * implementations of this interface.
 *
 * Business logic related to diagram reduction, or node re-usage,
 * should be handled by this entity.
 *
 * @author Jason Dellaluce
 */
internal interface BinaryDecisionDiagramBuilder<T : Comparable<T>> {

    /**
     * Returns an instance of [BinaryDecisionDiagram.Variable] with
     * the provided input.
     * */
    @JsName("buildVariable")
    fun buildVariable(
        value: T,
        low: BinaryDecisionDiagram<T>,
        high: BinaryDecisionDiagram<T>
    ): BinaryDecisionDiagram<T>

    /**
     * Returns an instance of [BinaryDecisionDiagram.Terminal] with
     * the provided input.
     * */
    @JsName("buildTerminal")
    fun buildTerminal(
        truth: Boolean
    ): BinaryDecisionDiagram<T>

    companion object {
        /**
         * Returns a simple [BinaryDecisionDiagramBuilder] instance that does
         * not apply platform-specific or reduction optimizations. This
         * provides basic means to build represent BDDs, and keeps the entire
         * data structure in memory in the form of a directed graph.
         * */
        @JsName("simpleOf")
        fun <E : Comparable<E>> simpleOf(): BinaryDecisionDiagramBuilder<E> {
            return SimpleBinaryDecisionDiagramBuilder()
        }

        /**
         * Returns a [BinaryDecisionDiagramBuilder] instance that applies
         * reduction optimizations through the `reduce` algorithm, and
         * delegates the construction of each node to [delegate]. By default,
         * [delegate] is set as [simpleOf]. The following reductions are
         * performed:
         * - Removal of duplicate variable nodes
         * - Removal of duplicate terminal nodes
         * - Removal of redundant variable nodes, which are
         * [BinaryDecisionDiagram.Variable] nodes where low and high point
         * to the same node
         * */
        @JsName("reducedOf")
        fun <E : Comparable<E>> reducedOf(
            delegate: BinaryDecisionDiagramBuilder<E> = simpleOf()
        ): BinaryDecisionDiagramBuilder<E> {
            return ReducedBinaryDecisionDiagramBuilder(delegate)
        }
    }
}
