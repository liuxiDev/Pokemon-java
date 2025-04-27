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
    private final int WIDTH = 32;
    private final int HEIGHT = 48;
    
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
     * 加载玩家精灵图
     */
    private void loadPlayerSprites() {
        // 加载玩家精灵表（不以/开头）
        playerSheet = ImageLoader.loadImage("image/player/player.png");
        
        // 如果加载失败，创建一个空白图像避免空指针异常
        if (playerSheet == null) {
            playerSheet = new BufferedImage(WIDTH * 3, HEIGHT * 4, BufferedImage.TYPE_INT_ARGB);
            System.err.println("警告：无法加载玩家图片，使用占位图像");
        }
        
        // 初始化方向对应的精灵图数组
        playerSprites = new HashMap<>();
        
        // 假设精灵表是3列4行，每行对应一个方向，每列是不同的动画帧
        // 第一行：向下行走(DOWN)
        // 第二行：向左行走(LEFT)
        // 第三行：向右行走(RIGHT)
        // 第四行：向上行走(UP)
        
        try {
            // 向下行走动画帧
            BufferedImage[] downFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                downFrames[i] = ImageLoader.getSubImage(playerSheet, i * WIDTH, 0, WIDTH, HEIGHT);
            }
            playerSprites.put(Direction.DOWN, downFrames);
            
            // 向左行走动画帧
            BufferedImage[] leftFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                leftFrames[i] = ImageLoader.getSubImage(playerSheet, i * WIDTH, HEIGHT, WIDTH, HEIGHT);
            }
            playerSprites.put(Direction.LEFT, leftFrames);
            
            // 向右行走动画帧
            BufferedImage[] rightFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                rightFrames[i] = ImageLoader.getSubImage(playerSheet, i * WIDTH, HEIGHT * 2, WIDTH, HEIGHT);
            }
            playerSprites.put(Direction.RIGHT, rightFrames);
            
            // 向上行走动画帧
            BufferedImage[] upFrames = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                upFrames[i] = ImageLoader.getSubImage(playerSheet, i * WIDTH, HEIGHT * 3, WIDTH, HEIGHT);
            }
            playerSprites.put(Direction.UP, upFrames);
        } catch (Exception e) {
            System.err.println("加载玩家动画帧时出错: " + e.getMessage());
            e.printStackTrace();
            
            // 出错时创建简单的单帧动画
            BufferedImage[] defaultFrame = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                defaultFrame[i] = createPlaceholderImage();
            }
            
            // 为所有方向设置相同的占位图
            playerSprites.put(Direction.DOWN, defaultFrame);
            playerSprites.put(Direction.LEFT, defaultFrame);
            playerSprites.put(Direction.RIGHT, defaultFrame);
            playerSprites.put(Direction.UP, defaultFrame);
        }
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
        y -= SPEED;
        moving = true;
    }
    
    public void moveDown() {
        direction = Direction.DOWN;
        y += SPEED;
        moving = true;
    }
    
    public void moveLeft() {
        direction = Direction.LEFT;
        x -= SPEED;
        moving = true;
    }
    
    public void moveRight() {
        direction = Direction.RIGHT;
        x += SPEED;
        moving = true;
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