@echo off
echo 正在启动口袋妖怪绿宝石游戏...

REM 确保资源目录存在
call create_folders.bat

REM 尝试使用Maven编译运行
IF EXIST "mvn" (
    call mvn clean compile exec:java -Dexec.mainClass="com.pokemon.Main"
) ELSE (
    REM 如果没有Maven，尝试直接使用Java运行
    IF EXIST "target\classes\com\pokemon\Main.class" (
        java -cp target/classes com.pokemon.Main
    ) ELSE (
        echo 找不到编译后的类文件，请先编译项目。
        echo 您可以使用以下命令编译并运行:
        echo mvn clean compile exec:java -Dexec.mainClass="com.pokemon.Main"
        pause
    )
)

pause 