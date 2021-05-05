package network.cow.cowmands

import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Tobias Büser
 */
internal class SpigotCowmand(val plugin: JavaPlugin, val command: Cowmand) :
    BukkitCommand(command.label, command.description, command.usage, command.aliases),
    PluginIdentifiableCommand {

    init {
        this.permission = command.permission
    }

    override fun execute(sender: CommandSender, label: String, argsArray: Array<out String>): Boolean {
        command.Executor(sender, Arguments(argsArray)).execute()
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, argsArray: Array<out String>): List<String> {
        return command.Executor(sender, Arguments(argsArray)).tabComplete()
    }

    override fun getPlugin(): Plugin {
        return plugin
    }


}
