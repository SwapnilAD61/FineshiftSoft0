package it.unibo.tuprolog.core.impl

import it.unibo.tuprolog.core.Rule
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Term

internal open class RuleImpl(
    override val head: Struct,
    override val body: Term,
    tags: Map<String, Any> = emptyMap()
) : ClauseImpl(head, body, tags), Rule {
    override fun replaceTags(tags: Map<String, Any>): Rule {
        return RuleImpl(head, body, tags)
    }
}
