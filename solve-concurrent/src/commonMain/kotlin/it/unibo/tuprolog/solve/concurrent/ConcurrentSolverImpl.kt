package it.unibo.tuprolog.solve.concurrent

import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.operators.OperatorSet
import it.unibo.tuprolog.solve.Solution
import it.unibo.tuprolog.solve.SolveOptions
import it.unibo.tuprolog.solve.channel.InputChannel
import it.unibo.tuprolog.solve.channel.InputStore
import it.unibo.tuprolog.solve.channel.OutputChannel
import it.unibo.tuprolog.solve.channel.OutputStore
import it.unibo.tuprolog.solve.concurrent.fsm.EndState
import it.unibo.tuprolog.solve.concurrent.fsm.State
import it.unibo.tuprolog.solve.concurrent.fsm.StateGoalSelection
import it.unibo.tuprolog.solve.concurrent.fsm.toGoals
import it.unibo.tuprolog.solve.currentTimeInstant
import it.unibo.tuprolog.solve.exception.Warning
import it.unibo.tuprolog.solve.flags.FlagStore
import it.unibo.tuprolog.solve.getAllOperators
import it.unibo.tuprolog.solve.impl.AbstractSolver
import it.unibo.tuprolog.solve.library.Libraries
import it.unibo.tuprolog.solve.toOperatorSet
import it.unibo.tuprolog.theory.MutableTheory
import it.unibo.tuprolog.theory.Theory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized
import kotlinx.coroutines.channels.Channel as KtChannel

internal open class ConcurrentSolverImpl(
    libraries: Libraries = Libraries.empty(),
    flags: FlagStore = FlagStore.empty(),
    initialStaticKb: Theory = Theory.empty(),
    initialDynamicKb: Theory = MutableTheory.empty(),
    inputChannels: InputStore = InputStore.fromStandard(),
    outputChannels: OutputStore = OutputStore.fromStandard(),
    trustKb: Boolean = false
) : ConcurrentSolver, AbstractSolver<ConcurrentExecutionContext>(
    libraries,
    flags,
    initialStaticKb,
    initialDynamicKb,
    inputChannels,
    outputChannels,
    trustKb
) {

    constructor(
        libraries: Libraries = Libraries.empty(),
        flags: FlagStore = FlagStore.empty(),
        staticKb: Theory = Theory.empty(),
        dynamicKb: Theory = MutableTheory.empty(),
        stdIn: InputChannel<String> = InputChannel.stdIn(),
        stdOut: OutputChannel<String> = OutputChannel.stdOut(),
        stdErr: OutputChannel<String> = OutputChannel.stdErr(),
        warnings: OutputChannel<Warning> = OutputChannel.warn(),
        trustKb: Boolean = false
    ) : this(
        libraries,
        flags,
        staticKb,
        dynamicKb,
        InputStore.fromStandard(stdIn),
        OutputStore.fromStandard(stdOut, stdErr, warnings),
        trustKb
    )

    @get:Synchronized
    @set:Synchronized
    override lateinit var currentContext: ConcurrentExecutionContext

    // override val resolutionScope = CoroutineScope(Dispatchers.Default)

    private fun CoroutineScope.handleAsyncStateTransition(state: State, handle: ConcurrentResolutionHandle): Job =
        launch {
            if (state is EndState) {
                handle.publishSolutionAndTerminateResolutionIfNeed(state.solution, this)
            } else {
                state.next().forEach { handleAsyncStateTransition(it, handle) }
            }
        }

    private suspend fun startAsyncResolution(initialState: State, handle: ConcurrentResolutionHandle) = coroutineScope {
        handleAsyncStateTransition(initialState, handle).join()
        handle.publishNoSolutionIfNeeded(initialState.context.query)
        handle.closeSolutionChannelIfNeeded()
    }

    private fun initialState(goal: Struct, options: SolveOptions): State {
        currentContext = ConcurrentExecutionContext(
            goals = goal.toGoals(),
            step = 1,
            query = goal,
            libraries = libraries,
            flags = flags,
            staticKb = staticKb.toImmutableTheory(),
            dynamicKb = dynamicKb.toMutableTheory(),
            operators = operators,
            inputChannels = inputChannels,
            outputChannels = outputChannels,
            customData = currentContext.customData,
            maxDuration = options.timeout,
            startTime = currentTimeInstant()
        )
        return StateGoalSelection(currentContext)
    }

    override fun solveConcurrently(
        goal: Struct,
        options: SolveOptions,
        channel: KtChannel<Solution>
    ): ReceiveChannel<Solution> {
        val initialState = initialState(goal, options)
        val handle = ConcurrentResolutionHandle(options, channel)
        val resolutionScope = CoroutineScope(Dispatchers.Default)
        resolutionScope.launch {
            startAsyncResolution(initialState, handle)
        }
        // val channel2 = resolutionScope.appendNo(channel, initialState.context.query)
        return channel //.appendNo(channel, initialState.context.query)
    }

    // @OptIn(ExperimentalCoroutinesApi::class)
    // private fun CoroutineScope.appendNo(solutions: ReceiveChannel<Solution>, query: Struct): ReceiveChannel<Solution> =
    //     produce(resolutionScope.coroutineContext, KtChannel.UNLIMITED) {
    //         var counter = 0
    //         for (solution in solutions) {
    //             counter += 1
    //             send(solution)
    //         }
    //         if (counter == 0) {
    //             send(Solution.no(query))
    //         }
    //     }

    override fun solveImpl(goal: Struct, options: SolveOptions): Sequence<Solution> {
        return solveConcurrently(goal, options).toSequence()
    }

    override fun copy(
        libraries: Libraries,
        flags: FlagStore,
        staticKb: Theory,
        dynamicKb: Theory,
        stdIn: InputChannel<String>,
        stdOut: OutputChannel<String>,
        stdErr: OutputChannel<String>,
        warnings: OutputChannel<Warning>
    ) = ConcurrentSolverImpl(libraries, flags, staticKb, dynamicKb, stdIn, stdOut, stdErr, warnings)

    override fun clone(): ConcurrentSolverImpl = copy()

    override fun initializeContext(
        libraries: Libraries,
        flags: FlagStore,
        staticKb: Theory,
        dynamicKb: Theory,
        operators: OperatorSet,
        inputChannels: InputStore,
        outputChannels: OutputStore,
        trustKb: Boolean
    ): ConcurrentExecutionContext = ConcurrentExecutionContext(
        libraries = libraries,
        flags = flags,
        staticKb = if (trustKb) staticKb.toImmutableTheory() else Theory.empty(),
        dynamicKb = if (trustKb) dynamicKb.toMutableTheory() else MutableTheory.empty(),
        operators = getAllOperators(libraries).toOperatorSet(),
        inputChannels = inputChannels,
        outputChannels = outputChannels
    )
}
