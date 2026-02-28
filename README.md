# Conveyor Belts — Minecraft Forge Mod

A Minecraft Forge mod for **1.21.1 / Forge 61.1.0** that adds a complete conveyor belt automation system.

## Blocks

| Block | Description |
|---|---|
| **Conveyor Belt** | Straight belt — moves items in the facing direction. 3 speed tiers (slow/normal/fast). |
| **Corner Belt** | Turns items 90° left or right. |
| **Slope Belt** | Moves items up or down one block. |
| **Splitter Belt** | One input, two outputs — alternates items left/right. |
| **Merger Belt** | Two inputs merge into one output. |
| **Detector Belt** | Emits a redstone signal when items are present. |
| **Conveyor Chest** | Inventory block — hopper-compatible, ejects items onto the belt in front. |
| **Vacuum Plate** | Pulls dropped items within 3 blocks onto the connected belt. |

## Tips

- Belts stop when powered by a redstone signal (except Detector Belt which emits redstone).
- Right-click a belt with a **Wrench** (crafted with iron ingots) to rotate it.
- Conveyor Chest accepts input from hoppers on the top/sides and ejects forward onto a belt.
- Speed tiers: craft with **Iron** (slow), **Gold** (normal), **Diamond** (fast) frames.

## Building the JAR

Minecraft mods are distributed as `.jar` files. To build one from this source:

### Requirements
- [JDK 21](https://adoptium.net/) — must be installed and on your PATH
- [Forge MDK 61.1.0](https://files.minecraftforge.net/) — provides the Gradle wrapper and build tooling

### Steps

**1. Download the Forge MDK**

Go to [files.minecraftforge.net](https://files.minecraftforge.net), find Minecraft **1.21.1**, and download the **MDK** zip for Forge **61.1.0**.

**2. Extract the MDK**

Extract the zip to a temporary folder. You'll see files like `gradlew`, `gradlew.bat`, and a `gradle/` folder.

**3. Clone this repo**

```bash
git clone https://github.com/Deacyde/Minecraft-Mod---Conveyor-Belts.git
cd Minecraft-Mod---Conveyor-Belts
```

**4. Copy the Gradle wrapper from the MDK into this folder**

```
gradlew          ← copy from MDK
gradlew.bat      ← copy from MDK (Windows)
gradle/          ← copy the whole folder from MDK
```

**5. Build**

```bash
# macOS / Linux
./gradlew build

# Windows
gradlew.bat build
```

The first run downloads ~500 MB of Minecraft internals — this is normal and only happens once.

**6. Find your JAR**

```
build/libs/conveyor-belts-1.0.0.jar
```

**7. Install**

Drop the JAR into your Minecraft `mods/` folder. Make sure you have [Forge 61.1.0 for MC 1.21.1](https://files.minecraftforge.net/) installed.

```
%APPDATA%\.minecraft\mods\          ← Windows
~/.minecraft/mods/                  ← macOS / Linux
```

Launch Minecraft with the Forge profile — the mod will appear in the Mods menu.

---

### IDE Setup (optional, for development)

To open the project in IntelliJ IDEA or Eclipse, run one of these after copying the MDK gradle files:

```bash
./gradlew genIntellijRuns   # IntelliJ IDEA
./gradlew genEclipseRuns    # Eclipse
```

Then open the project as a Gradle project in your IDE. Use the generated **runClient** run configuration to launch Minecraft with the mod loaded.

## License

MIT
