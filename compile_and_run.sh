#!/bin/bash
echo "============================================"
echo "  Foodie's Restaurant - Food Ordering System"
echo "============================================"
echo ""

# Create output directory
mkdir -p out

# Compile
echo "Compiling..."
javac -d out -sourcepath src src/foodordering/*.java

if [ $? -ne 0 ]; then
    echo "Compilation FAILED. Make sure JDK is installed."
    exit 1
fi

echo "Compilation successful!"
echo ""
echo "Starting application..."
java -cp out foodordering.Main
