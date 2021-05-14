#!/bin/sh

echo "What is your available RAM?"
read RAM

mkdir ../server
cd ../server/
curl https://papermc.io/api/v1/paper/1.16.5/latest/download --output paper.jar

echo "eula=true" > eula.txt
echo "java -Xms${RAM}G -Xmx${RAM}G -jar paper.jar --nogui" > start.sh
chmod +x start.sh

echo "Please stop the Server as it is fully started. Proceed with ENTER"
read Test

./start.sh

cd ../setup

sh link.sh

echo "Finished"
