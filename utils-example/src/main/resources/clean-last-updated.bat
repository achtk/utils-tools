@echo off
rem create by NettQun

rem 这里写你的仓库路径
set REPOSITORY_PATH=D:\soft\apache\apache-maven-3.6.1-bin\repo
rem 正在搜索...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    del /s /q %%i
)
rem 搜索完毕
pause