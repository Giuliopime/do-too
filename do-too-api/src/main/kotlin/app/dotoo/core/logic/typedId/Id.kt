package app.dotoo.core.logic.typedId

interface Id<T> {
    /**
     * Cast Id<T> to Id<NewType>.
     */
    @Suppress("UNCHECKED_CAST")
    fun <NewType> cast(): Id<NewType> = this as Id<NewType>
}
