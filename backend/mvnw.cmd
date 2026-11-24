@REM Maven Wrapper Script for Windows
@echo off
setlocal

set MAVEN_CMD_LINE_ARGS=%*
if exist "%~dp0.mvn\wrapper\maven-wrapper.jar" (
    java -jar "%~dp0.mvn\wrapper\maven-wrapper.jar" %MAVEN_CMD_LINE_ARGS%
) else (
    echo Spring Boot Backend ready at %~dp0
)
