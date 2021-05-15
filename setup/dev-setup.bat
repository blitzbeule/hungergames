@echo off

set /p RAM="What is your available RAM? "

cd ..
mkdir server
cd server
curl https://papermc.io/api/v1/paper/1.16.5/latest/download --output paper.jar

echo "eula=true" > eula.txt
echo "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -Xms${RAM}G -Xmx${RAM}G -jar paper.jar --nogui" > start.bat

set /p TEST="Please stop the Server as it is fully started. Proceed with ENTER"

call start.bat

cd ..
cd setup

call link.bat

echo "Finished"
