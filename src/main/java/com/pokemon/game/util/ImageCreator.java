package com.pokemon.game.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像创建工具类，用于生成游戏所需的基本图片
 */
public class ImageCreator {

    public static void main(String[] args) {
        createMissingImages();
    }
    /**
     * 生成缺失的游戏图片
     */
    public static void createMissingImages() {
        // 创建资源目录
        createDirectories();
        
        // 创建玩家角色精灵图
        createPlayerSpriteSheet();
        
        // 创建战斗背景
        createBattleBackground();
        
        // 创建皮卡丘图片
        createPikachuImages();
    }

    /**
     * 创建所需的目录结构
     */
    private static void createDirectories() {
        String[] dirs = {
            "src/main/resources/image/player",
            "src/main/resources/image/map",
            "src/main/resources/image/battle",
            "src/main/resources/image/item/balls",
            "src/main/resources/image/pokemon",
            "src/main/resources/audio"
        };
        
        for (String dir : dirs) {
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("创建目录: " + dir);
            }
        }
    }

    /**
     * 创建玩家角色精灵图
     */
    private static void createPlayerSpriteSheet() {
        File playerFile = new File("src/main/resources/image/player/player-2.png");
        if (playerFile.exists()) {
            System.out.println("玩家角色图片已存在: " + playerFile.getAbsolutePath());
            return;
        }
        
        // 创建一个3x4的精灵图，每个单元格48x64像素（放大尺寸）
        // 3列表示每个方向3个动画帧
        // 4行表示4个方向：下、左、右、上
        int cellWidth = 48;
        int cellHeight = 64;
        int width = cellWidth * 3;
        int height = cellHeight * 4;
        BufferedImage spriteSheet = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = spriteSheet.createGraphics();
        
        // 设置平滑绘制
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 创建更像游戏人物的精灵图
        
        // 第一行：向下方向
        createTrainerSprite(g, 0, 0, cellWidth, cellHeight, Direction.DOWN, 0);
        createTrainerSprite(g, cellWidth, 0, cellWidth, cellHeight, Direction.DOWN, 1);
        createTrainerSprite(g, cellWidth * 2, 0, cellWidth, cellHeight, Direction.DOWN, 2);
        
        // 第二行：向左方向
        createTrainerSprite(g, 0, cellHeight, cellWidth, cellHeight, Direction.LEFT, 0);
        createTrainerSprite(g, cellWidth, cellHeight, cellWidth, cellHeight, Direction.LEFT, 1);
        createTrainerSprite(g, cellWidth * 2, cellHeight, cellWidth, cellHeight, Direction.LEFT, 2);
        
        // 第三行：向右方向
        createTrainerSprite(g, 0, cellHeight * 2, cellWidth, cellHeight, Direction.RIGHT, 0);
        createTrainerSprite(g, cellWidth, cellHeight * 2, cellWidth, cellHeight, Direction.RIGHT, 1);
        createTrainerSprite(g, cellWidth * 2, cellHeight * 2, cellWidth, cellHeight, Direction.RIGHT, 2);
        
        // 第四行：向上方向
        createTrainerSprite(g, 0, cellHeight * 3, cellWidth, cellHeight, Direction.UP, 0);
        createTrainerSprite(g, cellWidth, cellHeight * 3, cellWidth, cellHeight, Direction.UP, 1);
        createTrainerSprite(g, cellWidth * 2, cellHeight * 3, cellWidth, cellHeight, Direction.UP, 2);
        
        g.dispose();
        
        saveImage(spriteSheet, playerFile);
        System.out.println("创建玩家角色精灵图: " + playerFile.getAbsolutePath());
    }
    
    /**
     * 方向枚举
     */
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    /**
     * 创建训练师精灵
     */
    private static void createTrainerSprite(Graphics2D g, int x, int y, int width, int height, Direction direction, int frame) {
        // 使用白色背景色以突出角色
        g.setColor(new Color(255, 255, 255, 0)); // 透明背景
        g.fillRect(x, y, width, height);
        
        // 绘制身体 (调整大小以更贴合截图中的角色)
        Color skinColor = new Color(255, 222, 173); // 肤色
        Color hairColor = new Color(255, 255, 255); // 白发色
        Color shirtColor = new Color(255, 0, 0);    // 红色衣服
        Color pantsColor = new Color(0, 100, 200);  // 蓝色裤子
        
        int headSize = width / 2;
        int bodyWidth = width / 3;
        int bodyHeight = height / 3;
        int legWidth = width / 5;
        int legHeight = height / 3;
        
        // 绘制腿部
        g.setColor(pantsColor);
        
        // 根据帧数和方向绘制腿部以产生走路效果
        if (direction == Direction.DOWN || direction == Direction.UP) {
            // 走路动画：移动腿部
            if (frame == 0) {
                // 左腿向前
                g.fillRect(x + width/2 - legWidth - 2, y + height - legHeight, legWidth, legHeight);
                g.fillRect(x + width/2 + 2, y + height - legHeight + 5, legWidth, legHeight - 5);
            } else if (frame == 2) {
                // 右腿向前
                g.fillRect(x + width/2 - legWidth - 2, y + height - legHeight + 5, legWidth, legHeight - 5);
                g.fillRect(x + width/2 + 2, y + height - legHeight, legWidth, legHeight);
            } else {
                // 站立状态
                g.fillRect(x + width/2 - legWidth - 2, y + height - legHeight, legWidth, legHeight);
                g.fillRect(x + width/2 + 2, y + height - legHeight, legWidth, legHeight);
            }
        } else if (direction == Direction.LEFT) {
            // 左右走动时的腿部
            if (frame == 0 || frame == 2) {
                // 交替腿部位置
                int offset = (frame == 0) ? 5 : -5;
                g.fillRect(x + width/2 - legWidth + offset, y + height - legHeight, legWidth, legHeight);
            } else {
                g.fillRect(x + width/2 - legWidth, y + height - legHeight, legWidth, legHeight);
            }
        } else { // RIGHT
            if (frame == 0 || frame == 2) {
                // 交替腿部位置
                int offset = (frame == 0) ? -5 : 5;
                g.fillRect(x + width/2 - legWidth + offset, y + height - legHeight, legWidth, legHeight);
            } else {
                g.fillRect(x + width/2 - legWidth, y + height - legHeight, legWidth, legHeight);
            }
        }
        
        // 绘制身体
        g.setColor(shirtColor);
        g.fillRect(x + width/2 - bodyWidth/2, y + headSize, bodyWidth, bodyHeight);
        
        // 绘制手臂
        int armWidth = width / 8;
        int armHeight = height / 4;
        
        // 根据方向绘制手臂
        if (direction == Direction.DOWN) {
            g.fillRect(x + width/2 - bodyWidth/2 - armWidth, y + headSize, armWidth, armHeight);
            g.fillRect(x + width/2 + bodyWidth/2, y + headSize, armWidth, armHeight);
        } else if (direction == Direction.UP) {
            g.fillRect(x + width/2 - bodyWidth/2 - armWidth, y + headSize, armWidth, armHeight);
            g.fillRect(x + width/2 + bodyWidth/2, y + headSize, armWidth, armHeight);
        } else if (direction == Direction.LEFT) {
            // 左臂在前
            if (frame == 0 || frame == 2) {
                int offset = (frame == 0) ? 3 : -3;
                g.fillRect(x + width/2 - bodyWidth/2 - armWidth + offset, y + headSize, armWidth, armHeight);
            } else {
                g.fillRect(x + width/2 - bodyWidth/2 - armWidth, y + headSize, armWidth, armHeight);
            }
        } else { // RIGHT
            // 右臂在前
            if (frame == 0 || frame == 2) {
                int offset = (frame == 0) ? 3 : -3;
                g.fillRect(x + width/2 + bodyWidth/2 + offset, y + headSize, armWidth, armHeight);
            } else {
                g.fillRect(x + width/2 + bodyWidth/2, y + headSize, armWidth, armHeight);
            }
        }
        
        // 绘制头部
        g.setColor(skinColor);
        g.fillOval(x + width/2 - headSize/2, y + 5, headSize, headSize);
        
        // 绘制头发
        g.setColor(hairColor);
        g.fillOval(x + width/2 - headSize/2 - 2, y, headSize + 4, headSize/2);
        
        // 绘制帽子
        g.setColor(Color.RED);
        g.fillRect(x + width/2 - headSize/2 - 4, y + 2, headSize + 8, headSize/4);
        g.fillArc(x + width/2 - headSize/2 - 2, y - headSize/4, headSize + 4, headSize/2, 0, 180);
        
        // 根据方向绘制面部特征
        g.setColor(Color.BLACK);
        if (direction == Direction.DOWN) {
            // 绘制眼睛
            g.fillOval(x + width/2 - headSize/4, y + headSize/3, 3, 3);
            g.fillOval(x + width/2 + headSize/4 - 3, y + headSize/3, 3, 3);
            
            // 绘制嘴巴
            g.drawLine(x + width/2 - 2, y + headSize/2 + 3, x + width/2 + 2, y + headSize/2 + 3);
        } else if (direction == Direction.UP) {
            // 背面没有面部特征
        } else if (direction == Direction.LEFT) {
            // 侧面只绘制一只眼睛
            g.fillOval(x + width/2 - headSize/4, y + headSize/3, 3, 3);
            
            // 绘制侧脸
            g.drawLine(x + width/2 - headSize/4 - 1, y + headSize/2, x + width/2 - headSize/4 + 3, y + headSize/2);
        } else { // RIGHT
            // 侧面只绘制一只眼睛
            g.fillOval(x + width/2 + headSize/4 - 3, y + headSize/3, 3, 3);
            
            // 绘制侧脸
            g.drawLine(x + width/2 + headSize/4 - 4, y + headSize/2, x + width/2 + headSize/4, y + headSize/2);
        }
    }
    
    /**
     * 创建战斗背景图片
     */
    private static void createBattleBackground() {
        File bgFile = new File("src/main/resources/image/battle/background.png");
        if (bgFile.exists()) {
            System.out.println("战斗背景图片已存在: " + bgFile.getAbsolutePath());
            return;
        }
        
        BufferedImage bg = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bg.createGraphics();
        
        // 设置平滑绘制
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制草地背景
        GradientPaint gradientBg = new GradientPaint(
            0, 0, new Color(120, 220, 120),
            0, 600, new Color(200, 250, 200)
        );
        g.setPaint(gradientBg);
        g.fillRect(0, 0, 800, 600);
        
        // 绘制战斗场地地面
        g.setColor(new Color(150, 110, 70));
        g.fillRect(50, 350, 700, 150);
        
        // 绘制远处的山脉
        g.setColor(new Color(100, 180, 100));
        for (int i = 0; i < 5; i++) {
            int x = 100 + i * 150;
            int y = 150;
            g.fillOval(x - 80, y, 160, 120);
        }
        
        // 绘制小花
        g.setColor(Color.YELLOW);
        for (int i = 0; i < 20; i++) {
            int x = (int)(Math.random() * 800);
            int y = (int)(Math.random() * 300) + 50;
            drawFlower(g, x, y);
        }
        
        // 添加一些装饰
        g.setColor(new Color(80, 120, 80));
        g.fillRect(0, 450, 800, 20);
        
        g.dispose();
        
        saveImage(bg, bgFile);
        System.out.println("创建战斗背景图片: " + bgFile.getAbsolutePath());
    }
    
    /**
     * 绘制小花
     */
    private static void drawFlower(Graphics2D g, int x, int y) {
        Color originalColor = g.getColor();
        int size = 10;
        
        // 花瓣
        for (int i = 0; i < 6; i++) {
            int dx = (int)(Math.cos(Math.PI * 2 * i / 6) * size);
            int dy = (int)(Math.sin(Math.PI * 2 * i / 6) * size);
            g.fillOval(x + dx - 3, y + dy - 3, 6, 6);
        }
        
        // 花蕊
        g.setColor(new Color(255, 100, 0));
        g.fillOval(x - 3, y - 3, 6, 6);
        
        g.setColor(originalColor);
    }
    
    /**
     * 创建皮卡丘图片
     */
    private static void createPikachuImages() {
        File frontFile = new File("src/main/resources/image/pokemon/pikachu_front.png");
        File backFile = new File("src/main/resources/image/pokemon/pikachu_back.png");
        File iconFile = new File("src/main/resources/image/pokemon/pikachu_icon.png");
        
        if (!frontFile.exists()) {
            BufferedImage front = createPikachuImage(true);
            saveImage(front, frontFile);
            System.out.println("创建皮卡丘正面图片: " + frontFile.getAbsolutePath());
        }
        
        if (!backFile.exists()) {
            BufferedImage back = createPikachuImage(false);
            saveImage(back, backFile);
            System.out.println("创建皮卡丘背面图片: " + backFile.getAbsolutePath());
        }
        
        if (!iconFile.exists()) {
            BufferedImage icon = createPikachuIcon();
            saveImage(icon, iconFile);
            System.out.println("创建皮卡丘图标: " + iconFile.getAbsolutePath());
        }
    }
    
    /**
     * 创建皮卡丘图片
     * 
     * @param isFront 是否是正面
     * @return 皮卡丘图片
     */
    private static BufferedImage createPikachuImage(boolean isFront) {
        BufferedImage pikachu = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = pikachu.createGraphics();
        
        // 设置平滑绘制
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制身体
        g.setColor(Color.YELLOW);
        g.fillOval(30, 40, 60, 50);
        
        if (isFront) {
            // 正面：绘制头部
            g.fillOval(20, 10, 80, 60);
            
            // 绘制耳朵
            g.fillOval(10, 0, 30, 50);
            g.fillOval(80, 0, 30, 50);
            g.setColor(Color.BLACK);
            g.fillOval(20, 0, 10, 20);
            g.fillOval(90, 0, 10, 20);
            
            // 绘制眼睛
            g.fillOval(40, 30, 10, 10);
            g.fillOval(70, 30, 10, 10);
            
            // 绘制鼻子和嘴
            g.fillOval(55, 40, 10, 5);
            g.drawArc(45, 45, 30, 15, 0, 180);
            
            // 绘制脸颊
            g.setColor(Color.RED);
            g.fillOval(25, 40, 15, 15);
            g.fillOval(80, 40, 15, 15);
            
            // 绘制尾巴
            g.setColor(Color.YELLOW);
            int[] xPoints = {30, 10, 20, 0, 10};
            int[] yPoints = {65, 60, 50, 40, 70};
            g.fillPolygon(xPoints, yPoints, 5);
        } else {
            // 背面：绘制头部和背部
            g.fillOval(30, 10, 60, 60);
            
            // 绘制耳朵
            g.fillOval(20, 0, 30, 50);
            g.fillOval(70, 0, 30, 50);
            g.setColor(Color.BLACK);
            g.fillOval(30, 0, 10, 20);
            g.fillOval(80, 0, 10, 20);
            
            // 绘制尾巴
            g.setColor(Color.YELLOW);
            int[] xPoints = {90, 110, 100, 120, 100};
            int[] yPoints = {65, 60, 50, 40, 70};
            g.fillPolygon(xPoints, yPoints, 5);
            
            // 绘制条纹
            g.setColor(new Color(150, 100, 0));
            g.fillRect(40, 20, 40, 5);
            g.fillRect(50, 30, 20, 5);
        }
        
        // 绘制脚
        g.setColor(Color.YELLOW);
        g.fillOval(40, 80, 15, 20);
        g.fillOval(65, 80, 15, 20);
        
        g.dispose();
        return pikachu;
    }
    
    /**
     * 创建皮卡丘图标
     */
    private static BufferedImage createPikachuIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icon.createGraphics();
        
        // 设置平滑绘制
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制头部
        g.setColor(Color.YELLOW);
        g.fillOval(4, 4, 24, 20);
        
        // 绘制耳朵
        g.fillOval(2, 0, 10, 15);
        g.fillOval(20, 0, 10, 15);
        
        // 绘制眼睛
        g.setColor(Color.BLACK);
        g.fillOval(10, 10, 4, 4);
        g.fillOval(18, 10, 4, 4);
        
        // 绘制脸颊
        g.setColor(Color.RED);
        g.fillOval(6, 14, 6, 6);
        g.fillOval(20, 14, 6, 6);
        
        // 绘制嘴
        g.setColor(Color.BLACK);
        g.drawArc(12, 15, 8, 5, 0, 180);
        
        g.dispose();
        return icon;
    }
    
    /**
     * 保存图片到文件
     */
    private static void saveImage(BufferedImage image, File file) {
        try {
            // 确保目录存在
            file.getParentFile().mkdirs();
            
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            System.err.println("保存图片失败: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }
} 