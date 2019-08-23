package it.unibo.tuprolog.solve.solver.statemachine.state

/**
 * A wrapper class representing States that should not be executed again, because they've already executed their behaviour
 *
 * @author Enrico
 */
class AlreadyExecutedState(internal val wrappedState: State) : State by wrappedState {
    override val hasBehaved: Boolean = true

    override fun toString(): String = "AlreadyExecutedState of: $wrappedState"
}

/** Extension method to wrap a [State], marking it as already executed */
internal fun State.alreadyExecuted(): AlreadyExecutedState =
        (this as? AlreadyExecutedState)
                ?.let { it }
                ?: AlreadyExecutedState(this)
