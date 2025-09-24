# Oxono - Strategic Alignment Game

> *A simple yet formidable alignment game where diagonals don't exist*

## Game Overview

**Oxono** is a strategic board game for two players (Pink and Black) that combines tactical movement with pattern recognition. The objective is clear : be the first to align 4 pieces of the same color OR 4 pieces of the same symbol, but only horizontally or vertically - no diagonals allowed !

### Game Components
- **6×6 game board** with 36 spaces
  (Note that you can chose the size of the board in this project)
- **32 game pieces**: 16 pink pieces (8 crosses ✕, 8 circles ○) and 16 black pieces (8 crosses ✕, 8 circles ○)  
- **2 special totems**: Blue pieces marked with cross (✕) and circle (○)

## Technical Implementation

This project implements a complete Oxono game application using various design patterns.

### Architecture & Design Patterns

The project follows **MVP (Model-View-Presenter)** architecture with implementation of key design patterns:

- **Facade Pattern**: Clean interface between view and model complexity
- **Observer Pattern**: Real-time game state updates across different views
- **Command Pattern**: Undo/Redo functionality for strategic gameplay
- **Strategy Pattern**: Multiple AI difficulty levels with different behavioral strategies

### Key Features

#### Core Game Logic
- Complete rule validation and game state management
- Win condition detection (4-in-a-row by color or symbol)
- Special totem movement mechanics with enclave handling
- Turn-based gameplay with comprehensive error checking

#### Multiple Interfaces
- **Console Interface**: Text-based gameplay for testing and development
- **JavaFX GUI**: Graphical interface with interactive elements, music player and animations when winning
- **Smart Visual Feedback**: 
  - Green highlighting for valid moves
  - Red highlighting for invalid moves
  - Real-time game state display

#### AI Opponents
- **Level 0 (Random)**: Completely random move selection
- **Level 1+ (Strategic)**: AI that recognizes winning opportunities

#### Advanced Gameplay Features
- **Undo/Redo System**: Full move history with command pattern implementation
- **Game Abandonment**: Clean and proper game termination at any point
- **Real-time Statistics**: Remaining pieces, empty spaces, current player display

## How to Play

### Game Setup
1. Place both totems (✕ and ○) on the center marked spaces
2. Each player takes 16 pieces of their color
3. Pink player starts the game

### Turn Sequence
Each turn consists of two mandatory actions:
1. **Move a Totem**: Choose either totem (✕ or ○) and move it horizontally or vertically to any valid empty space
2. **Place a Piece**: Place one of your pieces with the same symbol as the moved totem on an adjacent empty space

### Winning Conditions
Achieve either:
- **4 pieces of your color** in a row (horizontally or vertically)
- **4 pieces of the same symbol** in a row (horizontally or vertically)

### Special Rules - Enclave Mechanics
When a totem becomes surrounded (enclave), special movement rules apply:
- Can jump over adjacent pieces to the first empty space
- If moved to another enclave position, pieces can be placed anywhere on the board
- Adds strategic depth and prevents game stalemates

## Technical Stack

- **Language**: Java
- **GUI Framework**: JavaFX for user interface
- **Testing**: JUnit for comprehensive unit testing
- **Documentation**: Complete Javadoc for all public methods
- **Version Control**: Git with regular commits and proper branching

## Learning Objectives Achieved

This project demonstrates :

- **Architecture**: Clean separation of concerns with MVC
- **Design Patterns**: Practical implementation of Facade, Observer, Command, and Strategy patterns
- **GUI Development**: Creating responsive and intuitive user interfaces
- **AI opponent Programming**: Implementing game AI with different difficulty levels
- **Testing**: Comprehensive unit testing for reliability
