@echo off
setlocal enabledelayedexpansion

set ZIPFILE=project.zip

if exist "%ZIPFILE%" del "%ZIPFILE%"

set FILELIST=files_to_zip.txt
if exist "%FILELIST%" del "%FILELIST%"

REM Collect all files except node_modules
for /r %%F in (*) do (
    set "p=%%F"
    echo !p! | findstr /i "node_modules" >nul
    if errorlevel 1 (
        echo "!p!" >> "%FILELIST%"
    )
)

powershell -command "Compress-Archive -Path (Get-Content '%FILELIST%') -DestinationPath '%ZIPFILE%' -Force"

del "%FILELIST%"

echo Done!
pause
