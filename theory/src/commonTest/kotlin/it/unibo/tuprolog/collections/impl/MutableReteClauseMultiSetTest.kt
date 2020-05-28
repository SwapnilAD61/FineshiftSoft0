package it.unibo.tuprolog.collections.impl

import it.unibo.tuprolog.collections.MutableClauseMultiSet
import it.unibo.tuprolog.collections.PrototypeClauseMultiSetTest
import kotlin.test.Test
import kotlin.test.todo

internal class MutableReteClauseMultiSetTest : PrototypeClauseMultiSetTest{

    private val prototype = PrototypeClauseMultiSetTest.prototype(
        MutableClauseMultiSet.Companion::empty,
        MutableClauseMultiSet.Companion::of
    )

    @Test
    override fun collectionHasTheCorrectSize() {
        prototype.collectionHasTheCorrectSize()
    }

    @Test
    override fun emptyCollectionIsEmpty() {
        prototype.emptyCollectionIsEmpty()
    }

    @Test
    override fun filledCollectionIsNotEmpty() {
        prototype.filledCollectionIsNotEmpty()
    }

    @Test
    override fun collectionIsEmptyAfterRemovingEveryElement() {
        prototype.collectionIsEmptyAfterRemovingEveryElement()
    }

    @Test
    override fun collectionContainsElement() {
        prototype.collectionContainsElement()
    }

    @Test
    override fun collectionDoesNotContainElement() {
        prototype.collectionDoesNotContainElement()
    }

    @Test
    override fun collectionContainsAllElement() {
        prototype.collectionContainsAllElement()
    }

    @Test
    override fun collectionDoesNotContainAllElement() {
        prototype.collectionDoesNotContainAllElement()
    }

    @Test
    override fun singleClauseAdditionToCollectionWorksCorrectly() {
        prototype.singleClauseAdditionToCollectionWorksCorrectly()
    }

    @Test
    override fun multipleClauseAdditionToCollectionWorksCorrectly() {
        prototype.multipleClauseAdditionToCollectionWorksCorrectly()
    }

    @Test
    override fun retrievingPresentSingleClauseRetrievesTheClause() {
        prototype.retrievingPresentSingleClauseRetrievesTheClause()
    }

    @Test
    override fun retrievingAbsentSingleClauseDoesNotAlterCollection() {
        prototype.retrievingAbsentSingleClauseDoesNotAlterCollection()
    }

    @Test
    override fun retrievingPresentClauseWithRetrieveAllWorksCorrectly() {
        prototype.retrievingPresentClauseWithRetrieveAllWorksCorrectly()
    }

    @Test
    override fun retrievingAbsentClauseWithRetrieveAllDoesNotAlterCollection() {
        prototype.retrievingAbsentClauseWithRetrieveAllDoesNotAlterCollection()
    }

    @Test
    override fun countingOnPresentClauseAnswerTheRightNumber() {
        prototype.countingOnPresentClauseAnswerTheRightNumber()
    }

    @Test
    override fun countingOnAbsentClauseAnswerZero() {
        prototype.countingOnAbsentClauseAnswerZero()
    }

    @Test
    override fun getWithPresentClauseReturnsTheCorrectSequence() {
        prototype.getWithPresentClauseReturnsTheCorrectSequence()
    }

    @Test
    override fun getWithAbsentClauseReturnsAnEmptySequence() {
        prototype.getWithAbsentClauseReturnsAnEmptySequence()
    }

    @Test
    override fun equalsIsOrderIndependent() {
        prototype.equalsIsOrderIndependent()
    }

    @Test
    override fun hashCodeIsOrderIndependent() {
        prototype.hashCodeIsOrderIndependent()
    }
}