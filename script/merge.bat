cd ../build/libs
del /f /s /q tmp
rmdir /s /q tmp
mkdir tmp
cd tmp
tar -xf ..\SybDiscordAPI-1.0.0-all.jar
tar -xf ..\SybDiscordAPI-1.0.0.jar
cd ../
del SybDiscordAPI-1.0.0-merged.jar
del tmp\module-info.class
jar -cvf SybDiscordAPI-1.0.0-merged.jar -C tmp .
del /f /s /q tmp
rmdir /s /q tmp