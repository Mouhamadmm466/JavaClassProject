# BeAcquired

**BeAcquired** is a Java-based board game inspired by the classic game "Acquire". It is a strategy game where players build companies, buy stocks, and merge corporations to become the wealthiest player.

![Game Preview](game_preview.png)

## Project Overview
This project was developed as a final project for a Java programming class. It demonstrates the use of:
- **JavaFX** for the Graphical User Interface (GUI).
- Object-Oriented Programming (OOP) principles.
- Game logic and state management.

## How to Play
1.  **Place a Tile**: On your turn, place one of your tiles on the board.
2.  **Form Companies**: If you place a tile next to another, you may form a new company.
3.  **Buy Stocks**: Purchase stocks in active companies to grow your investment.
4.  **Merge Companies**: When two companies connect, the larger one acquires the smaller one. Shareholders of the acquired company receive bonuses.
5.  **Win**: The game ends when all companies are safe or the board is full. The player with the most money wins!

## Installation & Running

### Prerequisites
- Java Development Kit (JDK) 11 or higher.
- JavaFX SDK (if not bundled with your JDK).

### Running the JAR
You can download the `BeAcquired.jar` file from the releases (if available) or build it yourself.

To run the game:
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar BeAcquired.jar
```
*Note: Replace `/path/to/javafx/lib` with the actual path to your JavaFX `lib` directory.*

### Building from Source
1.  Clone the repository.
2.  Ensure you have the JavaFX SDK installed.
3.  Run the provided `build_jar.bat` script (you may need to edit it to set your JavaFX path).

```bash
./build_jar.bat
```

## License
This project is for educational purposes.
