@echo off
echo Starting Ordexa Spring Boot Backend Server...
cd /d "%~dp0backend"
call mvnw.cmd spring-boot:run
pause
