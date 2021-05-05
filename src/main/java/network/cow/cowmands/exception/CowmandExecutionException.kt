package network.cow.cowmands.exception

/**
 * @author Tobias Büser
 */
open class CowmandExecutionException(message: String, val type: Type) : Exception(message) {

    enum class Type {

        NO_PERMISSION,
        EXCEPTION_THROWN

    }

}
