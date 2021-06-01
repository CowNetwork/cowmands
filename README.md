# Cowmands

We all know it and hate it: The Spigot/Bukkit command system with its `CommandExecutor` and `TabCompletor`. It's all fun and games, but with subcommands it just is tedious work.
For that purpose we have this small library, which allows to declare your commands and subcommands in a simple way and let it handle the Bukkit stuff for you.

# Quick Usage

First add following dependency to your `pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>network.cow.mc</groupId>
    <artifactId>cowmands</artifactId>
    <version>0.1.1</version>
  </dependency>
</dependencies>
```

Now you can simply create a class representing a root command:

```kotlin
class HelloCommand : Cowmand() {

    override val label = "hello"
    override val permission = "command.hello"
    override val usage = "[player]"

    override fun execute(sender: CommandSender, args: Arguments) {
        if (args.isEmpty()) {
            Bukkit.broadcast(Component.text("Hello World!"), "")
            return
        }

        val player = Bukkit.getPlayer(args[0])
        if (player == null) {
            sender.sendMessage(Component.text("This player is not online.").color(NamedTextColor.RED))
            return
        }
        
        player.sendMessage(Component.text("Hello!"))
    }

    override fun tabComplete(sender: CommandSender, args: Arguments): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }
    }

}
```

And register it via:

```kotlin
Cowmands.register(yourPluginInstance, HelloCommand())
```

# Subcommands

An important feature for more complex commands is creating subcommands. Let's take our example from above and create a subcommand for broadcasting:

```kotlin
class HelloBroadcastCommand : Cowmand() {

    override val label = "broadcast"
    override val permission = "command.hello.broadcast"
    
    override fun execute(sender: CommandSender, args: Arguments) {
        Bukkit.broadcast(Component.text("Hello World!"), "")
    }

}
```

As you can see, we can just create it like a root command, but now comes the catch: We have to declare it a subcommand in our root `HelloCommand` to make it available for executions:

```kotlin
class HelloCommand : Cowmand() {

    override val label = "hello"
    override val permission = "command.hello"
    override val usage = "(<player>|broadcast)"

    // here we list all of the available subcommands
    override val subCommands = listOf(HelloBroadcastCommand())

    override fun execute(sender: CommandSender, args: Arguments) {
        // now if we don't receive arguments, we just give an error message.
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("/hello (<player>|broadcast)").color(NamedTextColor.RED))
            return
        }

        // [...]
    }

}
```
