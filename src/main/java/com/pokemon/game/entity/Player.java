package com.pokemon.game.entity;

import com.pokemon.game.util.ImageLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 玩家类，表示游戏中的主角小智
 */
public class Player {
    
    // 玩家位置
    private int x;
    private int y;
    
    // 玩家大小
    private final int WIDTH = 48; // 增加尺寸
    private final int HEIGHT = 64; // 增加尺寸
    
    // 游戏地图的边界
    private int mapWidth;
    private int mapHeight;
    
    // 移动速度
    private final int SPEED = 4;
    
    // 玩家朝向
    private Direction direction = Direction.DOWN;
    private boolean moving = false;
    
    // 拥有的宝可梦列表
    private List<Pokemon> pokemons;
    
    // 玩家图片
    private BufferedImage playerSheet;
    private Map<Direction, BufferedImage[]> playerSprites;
    
    // 动画相关
    private int currentFrame = 0;
    private int animationDelay = 10;
    private int animationTick = 0;
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        
        // 初始宝可梦：皮卡丘
        pokemons = new ArrayList<>();
        pokemons.add(new Pokemon("皮卡丘", 25, 100, 10, 5));
        
        // 加载玩家精灵图
        loadPlayerSprites();
    }
    
    /**
     * 设置地图尺寸，用于边界检测
     */
    public void setMapBounds(int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;
    }
    
    /**
     * 加载玩家精灵图
     */
    private void loadPlayerSprites() {
        try {
            // 加载玩家精灵表（不以/开头）
            playerSheet = ImageLoader.loadImage("image/player/player.png");
            
            // 如果加载失败，创建一个空白图像避免空指针异常
            if (playerSheet == null) {
                playerSheet = new BufferedImage(WIDTH * 3, HEIGHT * 4, BufferedImage.TYPE_INT_ARGB);
                System.err.println("警告：无法加载玩家图片，使用占位图像");
            }
            
            // 初始化方向对应的精灵图数组
            playerSprites = new HashMap<>();
            
            // 确保精灵表尺寸正确
            int frameWidth = Math.min(WIDTH, playerSheet.getWidth() / 3);
            int frameHeight = Math.min(HEIGHT, playerSheet.getHeight() / 4);
            
            // 确保精灵表至少有足够的尺寸
            if (frameWidth <= 0 || frameHeight <= 0) {
                System.err.println("警告：精灵表尺寸错误: " + playerSheet.getWidth() + "x" + playerSheet.getHeight());
                createDefaultSprites();
                return;
            }
            
            // 向下行走动画帧
            BufferedImage[] downFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                int x = i * frameWidth;
                int y = 0;
                if (x < playerSheet.getWidth() && y + frameHeight <= playerSheet.getHeight()) {
                    downFrames[i] = ImageLoader.getSubImage(playerSheet, x, y, frameWidth, frameHeight);
                } else {
                    downFrames[i] = createPlaceholderImage();
                }
            }
            playerSprites.put(Direction.DOWN, downFrames);
            
            // 向左行走动画帧
            BufferedImage[] leftFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                int x = i * frameWidth;
                int y = frameHeight;
                if (x < playerSheet.getWidth() && y + frameHeight <= playerSheet.getHeight()) {
                    leftFrames[i] = ImageLoader.getSubImage(playerSheet, x, y, frameWidth, frameHeight);
                } else {
                    leftFrames[i] = createPlaceholderImage();
                }
            }
            playerSprites.put(Direction.LEFT, leftFrames);
            
            // 向右行走动画帧
            BufferedImage[] rightFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                int x = i * frameWidth;
                int y = frameHeight * 2;
                if (x < playerSheet.getWidth() && y + frameHeight <= playerSheet.getHeight()) {
                    rightFrames[i] = ImageLoader.getSubImage(playerSheet, x, y, frameWidth, frameHeight);
                } else {
                    rightFrames[i] = createPlaceholderImage();
                }
            }
            playerSprites.put(Direction.RIGHT, rightFrames);
            
            // 向上行走动画帧
            BufferedImage[] upFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                int x = i * frameWidth;
                int y = frameHeight * 3;
                if (x < playerSheet.getWidth() && y + frameHeight <= playerSheet.getHeight()) {
                    upFrames[i] = ImageLoader.getSubImage(playerSheet, x, y, frameWidth, frameHeight);
                } else {
                    upFrames[i] = createPlaceholderImage();
                }
            }
            playerSprites.put(Direction.UP, upFrames);
        } catch (Exception e) {
            System.err.println("加载玩家动画帧时出错: " + e.getMessage());
            e.printStackTrace();
            createDefaultSprites();
        }
    }
    
    /**
     * 创建默认的精灵图数组
     */
    private void createDefaultSprites() {
        // 为每个方向创建简单的单帧动画
        BufferedImage[] defaultFrames = new BufferedImage[3];
        for (int i = 0; i < 3; i++) {
            defaultFrames[i] = createPlaceholderImage();
        }
        
        // 为所有方向设置相同的占位图
        playerSprites = new HashMap<>();
        playerSprites.put(Direction.DOWN, defaultFrames.clone());
        playerSprites.put(Direction.LEFT, defaultFrames.clone());
        playerSprites.put(Direction.RIGHT, defaultFrames.clone());
        playerSprites.put(Direction.UP, defaultFrames.clone());
    }
    
    /**
     * 创建占位图像
     */
    private BufferedImage createPlaceholderImage() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.drawRect(2, 2, WIDTH-4, HEIGHT-4);
        g.drawString("Player", 5, HEIGHT/2);
        g.dispose();
        return image;
    }
    
    public void moveUp() {
        direction = Direction.UP;
        int newY = y - SPEED;
        
        // 边界检测
        if (newY >= 0) {
            y = newY;
            moving = true;
        }
    }
    
    public void moveDown() {
        direction = Direction.DOWN;
        int newY = y + SPEED;
        
        // 边界检测
        if (mapHeight > 0 && newY + HEIGHT <= mapHeight) {
            y = newY;
            moving = true;
        } else if (mapHeight == 0) {
            // 如果地图高度未设置，则不限制
            y = newY;
            moving = true;
        }
    }
    
    public void moveLeft() {
        direction = Direction.LEFT;
        int newX = x - SPEED;
        
        // 边界检测
        if (newX >= 0) {
            x = newX;
            moving = true;
        }
    }
    
    public void moveRight() {
        direction = Direction.RIGHT;
        int newX = x + SPEED;
        
        // 边界检测
        if (mapWidth > 0 && newX + WIDTH <= mapWidth) {
            x = newX;
            moving = true;
        } else if (mapWidth == 0) {
            // 如果地图宽度未设置，则不限制
            x = newX;
            moving = true;
        }
    }
    
    /**
     * 更新玩家状态，包括动画
     */
    public void update() {
        // 仅在移动时更新动画
        if (moving) {
            animationTick++;
            if (animationTick >= animationDelay) {
                animationTick = 0;
                currentFrame = (currentFrame + 1) % 3; // 3帧动画循环
            }
        } else {
            // 不移动时使用站立帧（通常是第二帧）
            currentFrame = 1;
        }
        
        // 每次更新后重置移动状态，由游戏循环中的输入处理再次设置
        moving = false;
    }
    
    public void render(Graphics2D g) {
        // 从当前方向的精灵图数组中获取当前帧
        BufferedImage currentSprite = playerSprites.get(direction)[currentFrame];
        
        // 绘制玩家精灵图
        g.drawImage(currentSprite, x, y, null);
    }
    
    // Getters and setters
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isMoving() {
        return moving;
    }
    
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    
    public int getWidth() {
        return WIDTH;
    }
    
    public int getHeight() {
        return HEIGHT;
    }
    
    public List<Pokemon> getPokemons() {
        return pokemons;
    }
    
    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }
} 