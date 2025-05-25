@echo off
echo Compiling Scientific Calculator...
javac src/ScientificCalculator.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running Scientific Calculator...
echo.
echo Keyboard Shortcuts:
echo - Numbers: 0-9 keys or numpad
echo - Operators: +, -, *, /, =
echo - Enter: Calculate result
echo - Backspace: Delete last digit
echo - Escape: Clear all
echo - Delete: Clear entry
echo - F6: Toggle keyboard input
echo - S: Sine
echo - C: Cosine
echo - T: Tangent
echo - And more...
echo.

java -cp src ScientificCalculator

pause