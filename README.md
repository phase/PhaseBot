#PhaseBot

[![Join the chat at https://gitter.im/phase/PhaseBot](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/phase/PhaseBot?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Issues in progress](https://badge.waffle.io/phase/PhaseBot.png?label=in%20progress&title=In%20Progress)](https://waffle.io/phase/PhaseBot)
[![Build Status](https://travis-ci.org/phase/PhaseBot.svg)](https://travis-ci.org/phase/PhaseBot)
[![volkswagen status](https://auchenberg.github.io/volkswagen/volkswargen_ci.svg?v=1)](https://github.com/auchenberg/volkswagen)

A Minecraft bot that does various tasks

[![Throughput Graph](https://graphs.waffle.io/phase/PhaseBot/throughput.svg)](https://waffle.io/phase/PhaseBot/metrics)

## Building
Just compiling the code:
```
gradle build
```

Creating a **runnable jar** (*may take ~20 minutes*):
```
gradle fatJar
```

## Current Features
You can check the [waffle](https://waffle.io/phase/PhaseBot) to see what I'm working on.
* Connecting to servers (1.8.X)
* Reading chat commands
* Complete Console w/ features
* Parsing JavaScript & Ruby code
* Script Engine to interact with the bot
* Movement/Pathfinding (A*)
* Looking
* Loading entities and handling entity movement
* Loading chunks and handling block changes
* Changing Slot
* Breaking/Placing Blocks
