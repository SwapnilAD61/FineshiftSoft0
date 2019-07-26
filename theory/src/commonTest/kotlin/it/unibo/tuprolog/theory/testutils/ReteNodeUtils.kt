package it.unibo.tuprolog.theory.testutils

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.theory.rete.ReteNode
import it.unibo.tuprolog.unify.Unification.Companion.matches
import kotlin.math.min
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Utils singleton for testing [ReteNode]
 *
 * @author Enrico
 */
internal object ReteNodeUtils {

    /** Contains some well-formed rules */
    internal val rules by lazy {
        listOf(
                Fact.of(Truth.`true`()),
                Fact.of(Truth.fail()),
                Fact.of(Atom.of("a")),
                Fact.of(Atom.of("other")),
                Fact.of(Struct.of("a", Atom.of("other"))),
                Fact.of(Struct.of("other", Integer.of(1))),
                Rule.of(Atom.of("a"), Atom.of("other")),
                Rule.of(Struct.of("a", Atom.of("other")), Atom.of("a")),
                Rule.of(Struct.of("f", Atom.of("a"), Struct.of("b", Var.of("X")), Atom.of("do_something_else"))),
                Rule.of(Struct.of("a", Integer.of(22)), Var.anonymous()),
                Rule.of(Struct.of("f", Atom.of("a")), Var.of("Variable")),
                Rule.of(Struct.of("f", Atom.of("a")), Var.of("Variable")),
                Rule.of(Struct.of("a", Var.anonymous()), Struct.of("b", Var.anonymous())),
                Rule.of(Struct.of("a", Atom.of("a")), Empty.set()),
                Rule.of(Struct.of("a", Atom.of("a")), Struct.of("other", Var.anonymous())),
                Rule.of(Struct.of("a", Atom.of("a")), Struct.of("a", Var.anonymous())),
                Rule.of(Struct.of("a", Atom.of("a")), Var.anonymous())
        )
    }

    /** Contains some well-formed rules with no args head */
    internal val noArgHeadedRules by lazy { rules.filter { it.head.isAtom } }

    /** Contains some well-formed wules with "a" functor */
    internal val aFunctorRules by lazy { rules.filter { it.head.functor == "a" } }

    /** Contains some well-formed wules with "f" functor */
    internal val fFunctorRules by lazy { rules.filter { it.head.functor == "f" } }

    /** Contains a map of queries and results crafted watching [rules] collection (NOTE: any modifications must be reviewed by hand) */
    internal val rulesQueryResultsMap by lazy {
        mapOf(
                Fact.of(Truth.`true`()) to listOf(Fact.of(Truth.`true`())),
                Fact.of(Empty.list()) to emptyList(),
                Rule.of(Struct.of("a", Atom.of("a")), Var.anonymous()) to rules.takeLast(5),
                Rule.of(Struct.of("a", Atom.of("other")), Var.anonymous()).run {
                    this to rules.filter { it matches this }
                },
                Rule.of(Struct.of("a", Var.anonymous()), Struct.of("b", Var.anonymous())).run {
                    this to rules.filter { it matches this }
                }
        )
    }

    /** Contains a map of queries and results made excluding queries with non "a" functor from [rulesQueryResultsMap] */
    internal val aFunctorRulesQueryResultsMap by lazy { rulesQueryResultsMap.filterKeys { it.head.functor == "a" } }

    /** Contains some well-formed directives */
    internal val directives by lazy {
        listOf(
                Directive.of(Truth.`true`()),
                Directive.of(Truth.fail()),
                Directive.of(Atom.of("a")),
                Directive.of(Atom.of("other")),
                Directive.of(Struct.of("a", Atom.of("other"))),
                Directive.of(Struct.of("other", Integer.of(1))),
                Directive.of(Atom.of("a"), Atom.of("other")),
                Directive.of(Struct.of("a", Atom.of("other")), Atom.of("a")),
                Directive.of(Struct.of("f", Atom.of("a"), Struct.of("b", Var.of("X")), Atom.of("do_something_else"))),
                Directive.of(Struct.of("a", Integer.of(22)), Var.anonymous()),
                Directive.of(Struct.of("f", Atom.of("a")), Var.of("Variable")),
                Directive.of(Struct.of("f", Atom.of("a")), Var.of("Variable")),
                Directive.of(Struct.of("a", Var.anonymous()), Struct.of("b", Var.anonymous())),
                Directive.of(Struct.of("a", Atom.of("a")), Empty.set()),
                Directive.of(Struct.of("a", Atom.of("a")), Struct.of("other", Var.anonymous())),
                Directive.of(Struct.of("a", Atom.of("a")), Struct.of("a", Var.anonymous())),
                Directive.of(Struct.of("a", Atom.of("a")), Var.anonymous())
        )
    }

    /** Contains a map of queries and results crafted watching [directives] collection (NOTE: any modifications must be reviewed by hand)*/
    internal val directivesQueryResultsMap by lazy {
        mapOf(
                Directive.of(Truth.`true`()) to listOf(Directive.of(Truth.`true`())),
                Directive.of(Empty.list()) to emptyList(),
                Directive.of(Struct.of("a", Atom.of("a")), Var.anonymous()) to directives.takeLast(5),
                Directive.of(Var.anonymous(), Struct.of("a", Atom.of("other"))).run {
                    this to directives.filter { it matches this }
                },
                Directive.of(Struct.of("a", Var.anonymous()), Struct.of("b", Var.anonymous())).run {
                    this to directives.filter { it matches this }
                }
        )
    }

    /** Contains well-formed mixed [rules] and [directives] */
    internal val mixedClauses by lazy { rules + directives }

    /** Contains a map of queries and results obtained by [rulesQueryResultsMap] and [directivesQueryResultsMap] */
    internal val mixedClausesQueryResultsMap by lazy { rulesQueryResultsMap + directivesQueryResultsMap }

    /** Asserts that rete node has correct elements count */
    internal fun assertReteNodeElementCount(reteNode: ReteNode<*, Clause>, expectedCount: Int) =
            assertEquals(expectedCount, reteNode.indexedElements.count())

    /** Asserts that rete node is empty */
    internal fun assertReteNodeEmpty(reteNode: ReteNode<*, out Clause>) {
        assertTrue(reteNode.indexedElements.none())
        assertTrue(reteNode.children.none())
    }

    /** Asserts that rete node clauses are the same as expected */
    internal fun assertReteNodeClausesCorrect(reteNode: ReteNode<*, out Clause>, expectedClauses: Iterable<Clause>) =
            assertEquals(expectedClauses.toList(), reteNode.indexedElements.toList())

    /** Asserts that calling [idempotentAction] onto [reteNode] results in no actual change */
    internal inline fun assertNoChangesInReteNode(reteNode: ReteNode<*, out Clause>, idempotentAction: ReteNode<*, Clause>.() -> Sequence<Clause>) {
        val beforeContents = reteNode.indexedElements.toList()

        @Suppress("UNCHECKED_CAST")  // nothing will be inserted, so it's safe
        val idempotentActionResult = (reteNode as ReteNode<*, Clause>).idempotentAction()

        assertTrue(idempotentActionResult.none())
        assertReteNodeClausesCorrect(reteNode, beforeContents)
    }

    /** Asserts that calling [removeAction] onto [reteNode] results in [removedExpected] elements removed */
    internal inline fun assertRemovedFromReteNode(
            reteNode: ReteNode<*, out Clause>,
            removedExpected: Iterable<Clause>,
            removeAction: ReteNode<*, Clause>.() -> Sequence<Clause>
    ) {
        val allClauses = reteNode.indexedElements.asIterable()
        val allClauseCount = allClauses.count()
        val remainingClausesExpected = allClauses - removedExpected

        @Suppress("UNCHECKED_CAST")  // nothing will be inserted, so it's safe
        val removedActual = (reteNode as ReteNode<*, Clause>).removeAction()

        assertEquals(removedExpected, removedActual.toList())
        assertReteNodeElementCount(reteNode, allClauseCount - removedExpected.count())
        assertReteNodeClausesCorrect(reteNode, remainingClausesExpected)
    }

    /** Asserts that calling [removeAction] onto [reteNode] results in [toRemoveMatched] [removeLimit] elements to be removed,
     * respecting the partial ordering; this means that removed elements can be taken in every order BUT respecting the partial order */
    internal inline fun assertRemovedFromReteNodeRespectingPartialOrder(
            reteNode: ReteNode<*, out Clause>,
            toRemoveMatched: Iterable<Clause>,
            removeLimit: Int = Int.MAX_VALUE,
            removeAction: ReteNode<*, Clause>.() -> Sequence<Clause>
    ) {
        val allClauses = reteNode.indexedElements.asIterable()
        val allClauseCount = allClauses.count()
        val correctNumberOfRemoved = min(toRemoveMatched.count(), removeLimit)

        @Suppress("UNCHECKED_CAST") // nothing will be inserted, so it's safe
        val removedActualSequence = (reteNode as ReteNode<*, Clause>).removeAction()
        assertReteNodeElementCount(reteNode, allClauseCount - correctNumberOfRemoved)

        val removedActual = partialOrderingHeadClauseMap(removedActualSequence.asIterable())
        val removeMatchExpected = partialOrderingHeadClauseMap(toRemoveMatched)

        val checkerMap = removedActual.mapValues {
            it.value.zip(removeMatchExpected[it.key] ?: emptyList())
        }
        val (actualRemovedList, expectedRemovedList) = checkerMap.values.flatten().unzip()

        assertEquals(expectedRemovedList, actualRemovedList)

        assertClauseHeadPartialOrderingRespected(allClauses - expectedRemovedList, reteNode.indexedElements.asIterable())
    }

    /** Asserts that [actualClauses] respect partial ordering (checking for Clauses head structural equality) imposed by [expectedClauses] iteration order */
    internal fun assertClauseHeadPartialOrderingRespected(expectedClauses: Iterable<Clause>, actualClauses: Iterable<Clause>) {
//        assertEquals(expectedClauses.toList().sorted(), reteNode.indexedElements.toList().sorted()) TODO enable after solving issue #29 and delete two below assertions
        assertTrue("\nExpected:\t$expectedClauses\nActual:\t\t$actualClauses") {
            expectedClauses.toList().containsAll(actualClauses.toList())
        }
        assertTrue(actualClauses.toList().containsAll(expectedClauses.toList()))

        actualClauses.forEachStructurallyEqualsHead(partialOrderingHeadClauseMap(expectedClauses).toMutableMap(),
                onPresentEntry = { clause, entry ->
                    when {
                        entry.value.none() -> fail("Clause $clause not indexed under its head Struct")
                        entry.value.first() == clause -> entry.setValue(entry.value - clause)
                        else -> fail("Partial ordering not respected: $clause should come after these ${entry.value - clause}")
                    }
                },
                onMissingEntry = { clause, _ -> fail("Clause $clause not expected among these: $expectedClauses") }
        )
    }

    /** Asserts that ReteTree node respects partial ordering (checking for Clauses head structural equality) imposed by [expectedClauses] iteration order */
    internal fun assertCorrectAndPartialOrderRespected(reteNode: ReteNode<*, out Clause>, expectedClauses: Iterable<Clause>) =
            assertClauseHeadPartialOrderingRespected(expectedClauses, reteNode.indexedElements.asIterable())

    /** Creates a Map containing for each structurallyEquals Clause.head the clauses ordered (according to [clauses] iteration order),
     * constructing the overall partial ordering */
    private fun partialOrderingHeadClauseMap(clauses: Iterable<Clause>): Map<Struct?, Iterable<Clause>> =
            mutableMapOf<Struct?, Iterable<Clause>>().also { resultMap ->
                clauses.forEachStructurallyEqualsHead(resultMap,
                        onPresentEntry = { clause, entry -> entry.setValue(entry.value + clause) },
                        onMissingEntry = { clause, map -> map[clause.head] = mutableListOf(clause) }
                )
            }.toMap()


    /** Utility function to iterate over clauses with partial ordering map, doing actions on found or missing entry */
    private inline fun Iterable<Clause>.forEachStructurallyEqualsHead(
            partialOrderingMap: MutableMap<Struct?, Iterable<Clause>>,
            onPresentEntry: (Clause, MutableMap.MutableEntry<Struct?, Iterable<Clause>>) -> Unit,
            onMissingEntry: (Clause, MutableMap<Struct?, Iterable<Clause>>) -> Unit
    ) {
        forEach { clause ->
            partialOrderingMap.entries.find { (clauseHead, _) ->
                clauseHead?.let { clause is Rule && it structurallyEquals clause.head }
                        ?: clause.isDirective
            }?.also { onPresentEntry(clause, it) } ?: onMissingEntry(clause, partialOrderingMap)
        }
    }
}
