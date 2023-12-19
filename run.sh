#!/bin/bash

# FILEPATH: /home/med/pokemed-cli/run.sh

cd dist/

# Check the flag
if [ "$1" = "server" ]; then
  # Run the server
  java server.Server
elif [ "$1" = "main" ]; then
  # Run the main Java file
  java cli.Main
elif [ "$1" = "gui" ]; then
  # Run the main Java file
  java guigui.GameGUI
else
  echo "Invalid flag. Please specify 'server' or 'main'."
fi
