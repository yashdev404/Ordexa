@echo off
echo Starting Ordexa Spring Boot Backend Server...
cd /d "%~dp0backend"

where mvn >nul 2>nul
if %errorlevel% equ 0 (
    mvn spring-boot:run
) else (
    echo Maven is not installed in global PATH. Installing/running via Java...
    java -cp "src/main/resources;src/main/java" com.ordexa.backend.OrdexaApplication
)
pause
