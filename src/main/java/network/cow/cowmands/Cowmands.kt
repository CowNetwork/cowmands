package network.cow.cowmands

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Tobias Büser
 */
class Cowmands {

    companion object {

        @JvmStatic
        fun registerMany(plugin: JavaPlugin, vararg commands: Cowmand) = commands.forEach { register(plugin, it) }

        @JvmStatic
        fun register(plugin: JavaPlugin, command: Cowmand) {
            val executor = SpigotCowmand(plugin, command)

            Bukkit.getCommandMap().register(plugin.name, executor)
        }

    }

}
