# Server Portals

Register serverside portals, which can be built using configured blocks, items to light them up and execute a command
when accessed. Works with vanilla clients. If this mod is installed on the client as well, the portal frame can get
displayed in different colors configured on the server.

NOTE v1.0 - installing on a client does not work yet due to the fact, that debugging locally with non-authenticated
clients is pretty insane. I'm currently working on a build script plugin to get local authentication to work, because
you cannot debug like this. I'm not going to waste hours of copy pasting jars around, without a debugger attached.


## Installation

To install this plugin **fabric**, **[fabric API](https://modrinth.com/mod/fabric-api)** and
**[owo-lib](https://modrinth.com/mod/owo-lib)** is required.

This mod is available on [modrinth](https://modrinth.com/mod/server-portals) with slug `server-portals`.
The project source is available on [github/michiruf](https://github.com/michiruf/MCServerPortals) with the latest
readme [here](https://github.com/michiruf/MCServerPortals/blob/master/README.md).

Internally this plugin is packed with [kyrptonaught's customportalapi](https://github.com/kyrptonaught/customportalapi),
[kyrptonaught's CustomPortalApi-Polymer](https://github.com/kyrptonaught/CustomPortalApi-Polymer) and
because of that with [Polymer](https://polymer.pb4.eu/).


## Usage instructions

<span style="color:darkred;font-weight:bold">
NOTE: The server must get restarted to load these changes!<br>
So after every change of the portals, restart the server (unfortunately)!
</span>

---
List configured portals:
```
/serverportals list
```

---
Register a portal
```
/serverportals register NAME FRAME_BLOCK LIGHT_UP_ITEM COLOR COMMAND
```
For example: `/serverportals register test minecraft:bone_block minecraft:ender_eye black "say hello"` will register a
portal, with bone blocks as frame, ender eyes as light up activation item to hold in hand while right-clicking and
printing out hello from the user that steps into it.

---
Unregister a portal by its index
```
/serverportals unregister NAME
```


## Example configuration file

The configuration is location in `/config/server-portals.json5`.
Note that changing the configuration while the server is running, will be overwritten by
commands that configure portals.
LogLevel is currently unused.

```json5
{
  "logLevel": 0,
  "portals": [
    {
      "index": "say_hello_portal",
      "frameBlockId": "minecraft:bone_block",
      "lightWithItemId": "minecraft:ender_eye",
      "color": 0,
      "command": "say \"hello world\""
    },
    {
      "index": "tp_portal",
      "frameBlockId": "minecraft:gold_block",
      "lightWithItemId": "minecraft:ender_eye",
      "color": 5592575,
      "command": "tp @p 0 100 0"
    }
  ]
}
```

## Usage together with ProxyCommands

ProxyCommands can be found [here](https://github.com/michiruf/MCProxyCommand). To create a portal to another server
use the command:
```
/serverportals register NAME BLOCK ITEM COLOR "proxycommand \"server SERVERNAME\""
```
For example:
```
/serverportals register server_creative minecraft:bone_block minecraft:ender_eye black "proxycommand \"server creative\""
```


## Known error reports in server logs

The customportalapi will check if a portal without a dimension is registered and will print this on every launch of
the server:
```
[xx:xx:xx] [main/INFO] (Minecraft) [STDOUT]: [customportalapi]ERROR: Dimension is null
```
The configured command will get executed nevertheless. 


## Changelog

Changelog per release cycle can be found [here](https://github.com/michiruf/MCServerPortals/blob/master/CHANGELOG.md).
This changelog contains information from one release to the next one.


## License

[MIT License](https://github.com/michiruf/MCServerPortals/blob/master/LICENSE)


## Further reading (for devs)

* LoomProductionEnv could be good for real testing
  https://github.com/DaemonicLabs/LoomProductionEnv
