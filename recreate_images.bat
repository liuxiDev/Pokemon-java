@echo off
echo 正在重新创建游戏图片资源...

REM 删除旧图片
echo 删除旧图片...
IF EXIST "src\main\resources\image\player\player.png" del "src\main\resources\image\player\player.png"
IF EXIST "src\main\resources\image\battle\background.png" del "src\main\resources\image\battle\background.png"
IF EXIST "src\main\resources\image\pokemon\pikachu_front.png" del "src\main\resources\image\pokemon\pikachu_front.png"
IF EXIST "src\main\resources\image\pokemon\pikachu_back.png" del "src\main\resources\image\pokemon\pikachu_back.png"
IF EXIST "src\main\resources\image\pokemon\pikachu_icon.png" del "src\main\resources\image\pokemon\pikachu_icon.png"

REM 创建目录结构
echo 创建目录结构...
call create_folders.bat

REM 启动游戏，图片会自动创建
echo 启动游戏，将自动创建所需图片...
call start_game.bat

echo 图片资源已重新创建完成!
pause 