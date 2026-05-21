@echo off
setlocal

set "PROJECT_DIR=%~dp0"
set "OUT_DIR=%PROJECT_DIR%out"
set "CP=%OUT_DIR%;%PROJECT_DIR%src;%PROJECT_DIR%lib\mysql-connector-j-9.6.0.jar;%PROJECT_DIR%lib\mysql-connector-java.jar"

if not exist "%OUT_DIR%\Main.class" (
    call "%PROJECT_DIR%compile.bat"
    if errorlevel 1 exit /b 1
)

java -cp "%CP%" Main
