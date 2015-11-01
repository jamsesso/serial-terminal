# Serial Terminal
A super simple, feature-less serial terminal. Wrote this utility to get around using Minicom or Screen.

## Building

```sh
mvn package
```

This will create a file called `serial-terminal-1.0.jar` inside of the `./target/` directory.

## Running

```sh
java -jar serial-terminal-1.0.jar [device] [baud] [data bits] [stop bits] [parity bits]
```