@echo off
rem Lancement de la demo du TP 3 (console UTF-8, sortie lisible).
chcp 65001 >nul
mode con: cols=100 lines=50
title TP3-Spring-IoC-OCP
cd /d "%~dp0"
if defined JAVA_HOME (set "JAVA=%JAVA_HOME%\bin\java.exe") else (set "JAVA=java")
"%JAVA%" -cp "target\classes;target\lib\*" app.Main
