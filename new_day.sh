#!/bin/bash

# The two-digit input string, e.g., "02"
input=$1

# Check if the input is provided and is two digits
if [[ ! $input =~ ^[0-9]{2}$ ]]; then
    echo "Error: Input must be two digits."
    exit 1
fi

cd src || exit

# Lowercase folder name (e.g., day02)
folder_name="day$input"

# exit if the folder already exists
if [ -d "$folder_name" ]; then
    echo "Error: Folder already exists."
    exit 1
fi

# Create a new directory with the lowercase folder name
cp -r day00 $folder_name

# Renaming files in the new directory
for file in $folder_name/Day00*; do
    new_file_name=$(echo $file | sed "s/Day00/Day$input/")
    mv "$file" "$new_file_name"
done

# Replace occurrences of day00 and Day00 with appropriate day in the .kt file
sed -i '' "s/Day00/Day$input/g" "$folder_name/Day$input.kt"
sed -i '' "s/day00/day$input/g" "$folder_name/Day$input.kt"

git add .

echo "Folder and files copied and renamed successfully."