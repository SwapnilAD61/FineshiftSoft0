package it.unibo.tuprolog.solve.fsm

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.solve.ExecutionContextImpl
import it.unibo.tuprolog.utils.Cursor

internal data class StateInit(override val context: ExecutionContextImpl) : AbstractState(context) {

    override fun computeNext(): State {
        return StateGoalSelection(
            context.copy(
                goals = context.query.toGoals(),
                rules = Cursor.empty(),
                primitives = Cursor.empty(),
                substitution = Substitution.empty(),
                parent = null,
                choicePoints = null,
                depth = 0,
                step = 0
            )
        )
    }
}