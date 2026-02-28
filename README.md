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

## Building

Requires JDK 21 and the Forge MDK.

```bash
./gradlew build
```

Output jar is in `build/libs/`.

## License

MIT
