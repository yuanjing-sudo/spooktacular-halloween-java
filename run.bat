@echo off
setlocal
if defined JAVA_HOME (
  set "JAVABIN=%JAVA_HOME%\bin"
) else (
  set "JAVABIN=C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot\bin"
)
if not exist classes mkdir classes
"%JAVABIN%\javac" -encoding UTF-8 -d classes src\spooktacular\engine\*.java src\spooktacular\data\*.java src\spooktacular\systems\*.java src\spooktacular\combat\*.java src\spooktacular\quests\*.java src\spooktacular\game\*.java src\spooktacular\app\*.java src\spooktacular\swiftport\*.java
if errorlevel 1 exit /b 1
"%JAVABIN%\java" -cp classes spooktacular.game.TestEngine
if errorlevel 1 exit /b 1
"%JAVABIN%\java" -cp classes spooktacular.app.AppTest
if errorlevel 1 exit /b 1
"%JAVABIN%\java" -cp classes spooktacular.game.VoxelMineTest
if errorlevel 1 exit /b 1
"%JAVABIN%\java" -cp classes spooktacular.app.SpookyApp
