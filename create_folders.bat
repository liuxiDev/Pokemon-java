@echo off
echo 正在创建必要的资源目录...

REM 创建主要资源目录
mkdir src\main\resources\image\player
mkdir src\main\resources\image\map
mkdir src\main\resources\image\battle
mkdir src\main\resources\image\item\balls
mkdir src\main\resources\image\pokemon
mkdir src\main\resources\audio

echo 资源目录创建完成!
echo 请将游戏资源文件放到对应目录下：
echo - 玩家角色图片放在 src\main\resources\image\player 目录
echo - 地图图片放在 src\main\resources\image\map 目录
echo - 战斗背景放在 src\main\resources\image\battle 目录
echo - 精灵球图片放在 src\main\resources\image\item\balls 目录
echo - 宝可梦图片放在 src\main\resources\image\pokemon 目录
echo - 音频文件放在 src\main\resources\audio 目录

pause 