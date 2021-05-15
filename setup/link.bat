cd ..

mklink ..\..\plugins\core\target\hungergames-1.0-SNAPSHOT.jar server\plugins\hungergames.jar
rd /s /q server\world\datapacks
mklink ..\..\datapacks server\world\datapacks