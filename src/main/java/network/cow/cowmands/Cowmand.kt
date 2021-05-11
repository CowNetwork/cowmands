package network.cow.cowmands

import network.cow.cowmands.exception.CowmandExceptionThrownException
import network.cow.cowmands.exception.CowmandExecutionException
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

/**
 * @author Tobias Büser
 */
abstract class Cowmand {

    /**
     * Label of the command, e.g. `gamemode`.
     */
    abstract val label: String

    /**
     * Available aliases to execute the command with.
     */
    open val aliases = listOf<String>()

    /**
     * A short, English description on what the command does.
     */
    open val description = ""

    /**
     * Describes what parameters are allowed for this command.
     * A complete usage string is `/$label $usage`.
     *
     * This only makes sense to use, if either no [subCommands] are
     * set or if the command supports arguments besides subcommanding.
     */
    open val usage = ""

    /**
     * List of subcommands this command supports.
     */
    open val subCommands = listOf<Cowmand>()

    /**
     * Permission to check against the [CommandSender].
     * If empty, no check will be executed.
     */
    open val permission = ""

    /**
     * Only executes the command at this level. The args are already sliced.
     */
    abstract fun execute(sender: CommandSender, args: Arguments)

    /**
     * Only completes at this level. The args are already sliced.
     */
    open fun tabComplete(sender: CommandSender, args: Arguments) = if (args.size == 1) {
        subCommandsLabels()
    } else emptyList()

    /**
     * Will be called, when an error occurs during the execution of the command.
     */
    open fun handleError(sender: CommandSender, args: Arguments, error: CowmandExecutionException) {
        if (error is CowmandExceptionThrownException) {
            error.exception.printStackTrace()
        }
    }

    fun subCommandsLabels() = this.subCommands.map { it.label }

    /**
     * Used to traverse the subcommands and find the right command
     * to execute actions on.
     */
    inner class Executor(val sender: CommandSender, val args: Arguments) {

        fun execute() = traverse({ command, args ->
            try {
                command.execute(sender, args)
            } catch (ex: Exception) {
                handleError(sender, args, CowmandExceptionThrownException(ex))
            }
        }, { handleError(sender, args, it) })

        fun tabComplete(): List<String> {
            val list = mutableListOf<String>()

            traverse({ cmd, args ->
                val completions = cmd.tabComplete(sender, args)

                if (!args.isEmpty()) {
                    val cursor = args.last()

                    StringUtil.copyPartialMatches(cursor, completions, list)
                }
            }, { handleError(sender, args, it) })
            return list
        }

        private fun traverse(apply: (Cowmand, Arguments) -> Unit, error: (CowmandExecutionException) -> Unit) {
            if (permission.isNotEmpty() && !sender.hasPermission(permission)) {
                error(CowmandExecutionException(
                    "${sender.name} does not have permission $permission",
                    CowmandExecutionException.Type.NO_PERMISSION
                ))
                return
            }

            if (args.isEmpty() || subCommands.isEmpty()) {
                apply(this@Cowmand, args)
                return
            }

            val arg = args[0].toLowerCase()
            subCommands.forEach {
                if (it.label == arg || it.aliases.contains(arg)) {
                    it.Executor(sender, args.slice(1)).traverse(apply, error)
                    return@traverse
                }
            }

            // subcommand not found
            apply(this@Cowmand, args)
        }

    }

}
