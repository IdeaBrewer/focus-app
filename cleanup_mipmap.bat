@echo off
cd /d D:\aa_code\Focus\app\src\main\res
echo Removing duplicate mipmap files...
del /f /q mipmap-mdpi\ic_launcher.png
del /f /q mipmap-mdpi\ic_launcher.xml
del /f /q mipmap-mdpi\ic_launcher_round.xml
del /f /q mipmap-hdpi\ic_launcher.xml
del /f /q mipmap-hdpi\ic_launcher_round.xml
del /f /q mipmap-xhdpi\ic_launcher.xml
del /f /q mipmap-xhdpi\ic_launcher_round.xml
del /f /q mipmap-xxhdpi\ic_launcher.xml
del /f /q mipmap-xxhdpi\ic_launcher_round.xml
del /f /q mipmap-xxxhdpi\ic_launcher.xml
del /f /q mipmap-xxxhdpi\ic_launcher_round.xml
echo Cleanup complete!
pause