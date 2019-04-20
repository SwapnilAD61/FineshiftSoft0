package it.unibo.tuprolog.core

internal class VarImpl(override val name: String, private val identifier: Int = instanceCount++) : TermImpl(), Var {

    companion object {
        var instanceCount = 0
    }

    override val isNameWellFormed: Boolean by lazy {
        Var.WELL_FORMED_NAME_PATTERN.matches(name)
    }

    override fun clone(): Var {
        return VarImpl(name)
    }

    override val completeName: String by lazy {
        "${name}__$identifier"
    }

    override val isAnonymous: Boolean = super.isAnonymous

    override fun toString(): String {
        return if (isNameWellFormed) name else "¿$name?"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as VarImpl

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}