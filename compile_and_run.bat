@echo off
echo ============================================
echo   Foodie's Restaurant - Food Ordering System
echo ============================================
echo.

REM Create output directory
if not exist "out" mkdir out

REM Compile
echo Compiling...
javac -d out -sourcepath src src\foodordering\*.java
if %errorlevel% neq 0 (
    echo Compilation FAILED. Make sure JDK is installed.
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Starting application...
java -cp out foodordering.Main
pause
