package network.cow.cowmands

/**
 * @author Tobias Büser
 */
class Arguments(private val list: List<String>) {

    val size: Int; get() = list.size

    constructor(array: Array<out String>) : this(array.toList())

    fun isEmpty() = this.size == 0

    operator fun get(index: Int): String {
        return list[index]
    }

    fun getOrNull(index: Int): String? {
        if (index < 0 || index >= size) {
            return null
        }
        return get(index)
    }

    fun get(index: Int, default: String): String {
        return getOrNull(index) ?: default
    }

    fun slice(fromIndex: Int): Arguments {
        if (fromIndex < 0) return this
        if (fromIndex >= list.size) return Arguments(listOf())
        return Arguments(list.slice(fromIndex until list.size))
    }

    override fun toString(): String {
        return list.toString()
    }

}
