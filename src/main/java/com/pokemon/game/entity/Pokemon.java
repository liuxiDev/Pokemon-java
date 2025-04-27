package com.pokemon.game.entity;

import com.pokemon.game.util.ImageLoader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * 宝可梦类，表示游戏中的宝可梦
 */
public class Pokemon {
    
    private String name;
    private int level;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    
    // 宝可梦图片
    private BufferedImage frontSprite;  // 正面(战斗中敌方)
    private BufferedImage backSprite;   // 背面(战斗中我方)
    private BufferedImage iconSprite;   // 小图标(菜单中)
    
    /**
     * 创建一个宝可梦
     * 
     * @param name 宝可梦名称
     * @param level 宝可梦等级
     * @param maxHp 最大生命值
     * @param attack 攻击力
     * @param defense 防御力
     */
    public Pokemon(String name, int level, int maxHp, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        
        // 加载宝可梦图片
        loadSprites();
    }
    
    /**
     * 加载宝可梦精灵图
     */
    private void loadSprites() {
        // 由于没有具体的宝可梦图片资源，这里简单处理
        // 实际应该根据宝可梦名称加载对应图片
        
        // 为了简化，这里使用简单的占位图
        // 实际项目中，应该使用正确的宝可梦图片路径
        
        try {
            // 如果有皮卡丘，尝试加载皮卡丘图片
            if (name.equals("皮卡丘")) {
                // 假设存在皮卡丘的图片（路径不要以/开头）
                frontSprite = ImageLoader.loadImage("image/pokemon/pikachu_front.png");
                backSprite = ImageLoader.loadImage("image/pokemon/pikachu_back.png");
                iconSprite = ImageLoader.loadImage("image/pokemon/pikachu_icon.png");
            }
            
            // 其他宝可梦使用默认图片
            if (frontSprite == null) {
                // 没有具体图片时创建占位用彩色方块
                frontSprite = createPlaceholderImage(name.hashCode());
            }
            
            if (backSprite == null) {
                backSprite = createPlaceholderImage(name.hashCode() + 50); // 不同的颜色
            }
            
            if (iconSprite == null) {
                iconSprite = createPlaceholderImage(name.hashCode() + 100); // 不同的颜色
            }
        } catch (Exception e) {
            System.err.println("加载宝可梦图片时出错: " + e.getMessage());
            frontSprite = createPlaceholderImage(name.hashCode());
            backSprite = createPlaceholderImage(name.hashCode() + 50);
            iconSprite = createPlaceholderImage(name.hashCode() + 100);
        }
    }
    
    /**
     * 创建占位图像
     * 
     * @param seed 随机种子，用于确定颜色
     * @return 占位图像
     */
    private BufferedImage createPlaceholderImage(int seed) {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        // 使用宝可梦名称作为种子生成不同颜色
        int r = (seed % 255);
        int g2 = ((seed / 255) % 255);
        int b = ((seed / (255 * 255)) % 255);
        
        g.setColor(new java.awt.Color(r, g2, b));
        g.fillRect(0, 0, 64, 64);
        
        // 绘制简单的宝可梦轮廓
        g.setColor(java.awt.Color.WHITE);
        g.drawOval(16, 8, 32, 32); // 头
        g.drawRect(24, 40, 16, 24); // 身体
        
        g.dispose();
        return image;
    }
    
    /**
     * 宝可梦受到伤害
     * 
     * @param damage 伤害值
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if(hp < 0) {
            hp = 0;
        }
    }
    
    /**
     * 治疗宝可梦
     * 
     * @param amount 恢复量
     */
    public void heal(int amount) {
        hp += amount;
        if(hp > maxHp) {
            hp = maxHp;
        }
    }
    
    /**
     * 升级
     */
    public void levelUp() {
        level++;
        maxHp += 5;
        attack += 2;
        defense += 1;
        hp = maxHp;
    }
    
    /**
     * 在指定位置渲染宝可梦
     * 
     * @param g 图形对象
     * @param x X坐标
     * @param y Y坐标
     * @param isFront 是否显示正面
     */
    public void render(Graphics2D g, int x, int y, boolean isFront) {
        BufferedImage sprite = isFront ? frontSprite : backSprite;
        g.drawImage(sprite, x, y, null);
    }
    
    /**
     * 渲染宝可梦图标
     * 
     * @param g 图形对象
     * @param x X坐标
     * @param y Y坐标
     */
    public void renderIcon(Graphics2D g, int x, int y) {
        g.drawImage(iconSprite, x, y, null);
    }
    
    // Getters and setters
    
    public String getName() {
        return name;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getHp() {
        return hp;
    }
    
    public int getMaxHp() {
        return maxHp;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public BufferedImage getFrontSprite() {
        return frontSprite;
    }
    
    public BufferedImage getBackSprite() {
        return backSprite;
    }
    
    public BufferedImage getIconSprite() {
        return iconSprite;
    }
} 