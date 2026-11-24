@REM Maven Wrapper Script for Windows
@echo off
setlocal

set MAVEN_CMD_LINE_ARGS=%*

if not exist "%~dp0.mvn\wrapper" mkdir "%~dp0.mvn\wrapper"

if not exist "%~dp0.mvn\wrapper\maven-wrapper.jar" (
    echo Downloading Maven Wrapper dependencies...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%~dp0.mvn\wrapper\maven-wrapper.jar'"
)

java -jar "%~dp0.mvn\wrapper\maven-wrapper.jar" %MAVEN_CMD_LINE_ARGS%
