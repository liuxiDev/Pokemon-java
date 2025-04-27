# 口袋妖怪绿宝石 Java版

这是一个使用Java实现的简化版口袋妖怪绿宝石游戏。

## 游戏背景

游戏讲述了来自真心镇的小智，带着他的宝可梦皮卡丘击败八个道馆后挑战四大天王成为宝可梦冠军的故事。

## 游戏功能

- 控制角色在游戏地图上移动
- 在草地区域随机遇到宝可梦
- 使用皮卡丘进行战斗
- 捕获新的宝可梦

## 操作方式

- 方向键：控制角色移动
- 回车键：确认选择
- ESC键：返回菜单

## 图像资源

游戏使用以下图像资源：
- `src/main/resources/image/player/player.png` - 玩家角色精灵图
- `src/main/resources/image/map/` - 地图图片
- `src/main/resources/image/item/balls/` - 各种精灵球图片

## 如何运行

### 使用批处理文件运行

1. 确保你已安装Java 8或更高版本
2. 双击`start_game.bat`文件启动游戏

### 使用Maven运行

1. 确保你已安装Java 8或更高版本和Maven
2. 在项目根目录下执行：
```
mvn clean compile exec:java -Dexec.mainClass="com.pokemon.Main"
```

### 在IDE中运行

1. 导入项目到Eclipse或IntelliJ IDEA
2. 运行`com.pokemon.Main`类

## 游戏截图

(实际游戏截图会在这里)

## 技术栈

- Java 8
- Java Swing (图形界面) 