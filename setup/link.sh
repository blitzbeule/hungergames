cd ..
ln -s ../../plugins/core/target/hungergames-1.0-SNAPSHOT.jar  server/plugins/hungergames.jar
rm -rf server/world/datapacks
ln -s ../../datapacks server/world/datapacks

cd server/plugins
curl https://dev.bukkit.org/projects/worldedit/files/3283695/download --output we.jar