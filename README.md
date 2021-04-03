# Jess

![Jess at startup](img/screenshot1_small.jpg)

**Note: current work on the project is dedicated to move it form a sandbox to an
actual, playable chess game versus the computer**

Jess is a free, open source chess sandbox, meaning you get a **standard chess
board without any rules nor mechanics**. Any move can be done at any time, no
matter the color or legality of the move. This makes it perfect to just **try
out some positions**
without having to move the opposing colors every second move.

## Technology

Jess is built using java, and a library
called [Salty Engine](https://www.github.com/sanj0/salty-engine).

Salty Engine is also free and open source and maintained by myself, downloading
and installing it using maven is thus needed to build and use Jess.

## How to install Jess

To use Jess you can either download an existing release from github or build
Jess yourself using git, maven and java.

1. download and install Salty Engine using git, maven and java
    1. clone the repository
        ```bash
        git clone https://www.github.com/sanj0/salty-engine
        ```
    2. install the engine
        ```bash
        cd salty-engine && mvn clean install
        ```
2. download and install Jess using git, maven and java
    1. clone the repository
        ```bash
        git clone https://www.github.com/sanj0/jess
        ```
    2. build Jess and copy the runnable jar
        ```bash
        cd jess
        mvn clean install
        JESS_EXECUTABLE_JAR=$(ls target | sort | grep ^jess | head -1)
        mkdir bin; cp target/${JESS_EXECUTABLE_JAR} bin/jess.jar
        ```

The executable Jess jar is now at `bin/jess.jar` and can be executed like any
regular jar using for example

```bash
java -jar bin/jess.jar
```

## How to use Jess
When started via a command line, a single passed argument makes Jess interpret
that argument as a FEN position, the game will start in this position. Note that
only the position will be processed and castle rights, en passant and color to move
will be ignored. Also Note that in case you have a complete FEN with spaces in between
the parts, you will have to either quote the whole string or remove the (anyway redundant)
additional parts so that the argument count is 1.
#### 1. How to move
You can move piece of the color of turn by dragging one of them to a legal destination
square. Once a piece was picked up, all legal moves will be highlighted
#### 2. How to make Chessica move
You can always make chessica move the current turn by pressing 'm' on your keyboard.
When you want her to make every following move of the color that is **not** currently
on turn, press 'a' (when 'a' is for example pressed before the first move, Chessica
will play every move for the black pieces). Pressing 'a' again will disable it.
#### 3. All keyboard shortcuts
- 'm': make chessica do the current move on turn
- 'a': (toggleable) make chessica play every next move by the color **not** currently on turn
- arrow left: undo a move (can mess up castle rights)
- arrow right: redo a move (can mess up castle rights)
- 'r': reset the game to the position Jess was started with
- 'i': invert the chess board

## Known issues
- castle rights and en passant are stored statically; therefore the AI doesn't know
these moves are possible until right before, they are thus not (or wrongly) calculated in
- castle rights are not correct, you can castle out of check and over check (not into check though)
- move generation is technically doubled as for every move, the engine has to calculate
every pseudo-legal response in order to tell if the move would result in a check on yourself
