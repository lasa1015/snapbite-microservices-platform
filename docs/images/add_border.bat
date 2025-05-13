@echo off
cd /d %~dp0

:: 创建一个保存结果的文件夹
set "OUT_DIR=edited"
if not exist "%OUT_DIR%" (
    mkdir "%OUT_DIR%"
)

:: 遍历所有 PNG 文件并处理
for %%i in (*.png) do (
    magick "%%i" -bordercolor "#ccc" -border 3 "%OUT_DIR%\edited_%%~ni.png"
)

echo ✅ All images processed and saved to the folder: %OUT_DIR%
pause
