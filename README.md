# Scientific Calculator

A Java-based scientific calculator with a user-friendly interface and advanced mathematical functions.

## Features

- **Basic Operations**: Addition, subtraction, multiplication, division
- **Advanced Functions**: Trigonometric (sin, cos, tan), logarithmic (log, ln), power, root, etc.
- **Memory Functions**: Store, recall, add to memory, subtract from memory
- **Angle Mode**: Switch between degrees and radians
- **Keyboard Input**: Enter calculations directly using keyboard
- **Modern UI**: Clean and responsive interface built with Java Swing
- **Enhanced Display**: Shows full expressions and calculation steps

## Project Structure

The project has a simple structure with a single Java file containing all the necessary code:

```
ScientificCalculator/
└── src/
    └── ScientificCalculator.java
```

## UI Components

- **Display Field**: Shows input and calculation results
- **Number Pad**: Buttons for digits 0-9 and decimal point
- **Basic Operations**: Addition, subtraction, multiplication, division
- **Scientific Functions**: Trigonometric, logarithmic, and other advanced functions
- **Memory Functions**: MC (Memory Clear), MR (Memory Recall), MS (Memory Store), M+ (Memory Add), M- (Memory Subtract)
- **Angle Mode**: Toggle between degrees and radians

## Keyboard Shortcuts

The calculator supports the following keyboard shortcuts:

- **Numbers**: 0-9 keys or numpad
- **Operators**: +, -, *, /, =
- **Enter**: Calculate result
- **Backspace**: Delete last digit
- **Escape**: Clear all
- **Delete**: Clear entry
- **F6**: Toggle keyboard input
- **S**: Sine
- **C**: Cosine
- **T**: Tangent
- **L**: Log (base 10)
- **N**: Natural Log
- **R**: Square Root
- **P**: Pi
- **E**: Euler's number
- **Shift+5**: Percentage

## How to Compile and Run

### Using Command Line

1. Navigate to the project directory:
   ```
   cd ScientificCalculator
   ```

2. Compile the Java file:
   ```
   javac src/ScientificCalculator.java
   ```

3. Run the application:
   ```
   java -cp src ScientificCalculator
   ```

### Using an IDE

1. Open the project in your preferred IDE (Eclipse, IntelliJ IDEA, NetBeans, etc.)
2. Build the project
3. Run the `ScientificCalculator` class

## Requirements

- Java Development Kit (JDK) 8 or higher
- Java Swing (included in JDK)

## Implementation Details

- **Java Swing**: Used for creating the graphical user interface
- **Event Handling**: ActionListener for button clicks
- **Mathematical Functions**: Utilizes Java's Math library for calculations
- **Responsive Design**: Properly sized components with appropriate spacing
- **Visual Appeal**: Color-coded buttons for different functions