@echo off
setlocal

set "PROJECT_DIR=%~dp0"
set "SRC_DIR=%PROJECT_DIR%src"
set "OUT_DIR=%PROJECT_DIR%out"
set "MYSQL_JAR=%PROJECT_DIR%lib\mysql-connector-j-9.6.0.jar"
set "MYSQL_ALIAS_JAR=%PROJECT_DIR%lib\mysql-connector-java.jar"

if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

if not exist "%MYSQL_JAR%" (
    echo Missing connector: %MYSQL_JAR%
    exit /b 1
)

if not exist "%MYSQL_ALIAS_JAR%" (
    echo Missing connector: %MYSQL_ALIAS_JAR%
    exit /b 1
)

rem The application references the MySQL driver by class name at runtime.
rem Keeping external jars off javac's classpath avoids a JDK 25 zipfs close warning on Windows.
javac -d "%OUT_DIR%" "%PROJECT_DIR%Main.java" ^
 "%SRC_DIR%\auth\Login.java" ^
 "%SRC_DIR%\db\DBConnection.java" ^
 "%SRC_DIR%\features\Enrollment.java" ^
 "%SRC_DIR%\features\Grades.java" ^
 "%SRC_DIR%\features\Section.java" ^
 "%SRC_DIR%\features\Student.java" ^
 "%SRC_DIR%\features\Subject.java" ^
 "%SRC_DIR%\features\SubjectOffering.java" ^
 "%SRC_DIR%\features\Teacher.java" ^
 "%SRC_DIR%\features\User.java" ^
 "%SRC_DIR%\model\Enrollment.java" ^
 "%SRC_DIR%\model\Grades.java" ^
 "%SRC_DIR%\model\Section.java" ^
 "%SRC_DIR%\model\Student.java" ^
 "%SRC_DIR%\model\Subject.java" ^
 "%SRC_DIR%\model\SubjectOffering.java" ^
 "%SRC_DIR%\model\Teacher.java" ^
 "%SRC_DIR%\model\User.java" ^
 "%SRC_DIR%\ui\Main.java" ^
 "%SRC_DIR%\ui\Menu.java"

if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

echo Compilation successful.
