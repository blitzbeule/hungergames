#!/bin/sh

echo "What is your available RAM?"
read RAM

mkdir ../server
cd ../server/
curl https://papermc.io/api/v1/paper/1.17/latest/download --output paper.jar

echo "eula=true" > eula.txt
echo "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -Xms${RAM}G -Xmx${RAM}G -jar paper.jar --nogui" > start.sh
chmod +x start.sh

echo "Please stop the Server as it is fully started. Proceed with ENTER"
read Test

./start.sh

cd ../setup

sh link.sh

echo "Finished"
