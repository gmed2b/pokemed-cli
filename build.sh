#!/bin/bash

# Set the source and destination directories
src_dir="src"
dist_dir="dist"

# Create the destination directory if it doesn't exist
mkdir -p "$dist_dir"

# Go to the source directory
cd "$src_dir"

# For each folder in the source directory
for folder in */; do
    javac "$folder"*.java -d ../"$dist_dir"
done

# Go back to the root directory
cd ..
echo "Build complete"
