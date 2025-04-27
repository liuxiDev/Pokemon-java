package com.pokemon.game.battle;

import com.pokemon.game.entity.Pokemon;
import com.pokemon.game.util.ImageLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 宝可梦战斗系统，处理战斗相关的逻辑计算
 */
public class BattleSystem {
    
    private Random random;
    
    // 精灵球图片
    private Map<String, BufferedImage> pokeBallImages;
    
    public BattleSystem() {
        random = new Random();
        loadBallImages();
    }
    
    /**
     * 加载精灵球图片
     */
    private void loadBallImages() {
        pokeBallImages = new HashMap<>();
        
        // 加载各种精灵球图片
        String[] ballTypes = {
            "poke_ball", "great_ball", "ultra_ball", "master_ball",
            "safari_ball", "net_ball", "dive_ball", "nest_ball",
            "repeat_ball", "timer_ball", "luxury_ball", "premier_ball"
        };
        
        for (String ballType : ballTypes) {
            // 路径不要以/开头
            BufferedImage ballImage = ImageLoader.loadImage("image/item/balls/" + ballType + ".png");
            if (ballImage != null) {
                pokeBallImages.put(ballType, ballImage);
            } else {
                System.err.println("无法加载精灵球图片: " + ballType);
                // 创建占位图像
                pokeBallImages.put(ballType, createBallPlaceholder(ballType));
            }
        }
    }
    
    /**
     * 创建精灵球占位图像
     */
    private BufferedImage createBallPlaceholder(String ballType) {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        // 根据球类型选择不同颜色
        Color ballColor;
        switch (ballType) {
            case "great_ball": 
                ballColor = new Color(0, 0, 200); // 蓝色
                break;
            case "ultra_ball": 
                ballColor = new Color(250, 250, 0); // 黄色
                break;
            case "master_ball": 
                ballColor = new Color(150, 0, 150); // 紫色
                break;
            default: 
                ballColor = new Color(200, 0, 0); // 红色(普通精灵球)
        }
        
        // 绘制球体
        g.setColor(ballColor);
        g.fillOval(2, 2, 28, 28);
        g.setColor(Color.WHITE);
        g.fillOval(10, 10, 12, 12);
        g.setColor(Color.BLACK);
        g.drawOval(2, 2, 28, 28);
        g.drawLine(2, 16, 30, 16);
        
        g.dispose();
        return image;
    }
    
    /**
     * 获取精灵球图片
     * 
     * @param ballType 精灵球类型
     * @return 精灵球图片
     */
    public BufferedImage getBallImage(String ballType) {
        return pokeBallImages.getOrDefault(ballType, pokeBallImages.get("poke_ball"));
    }
    
    /**
     * 计算伤害值
     * 
     * @param attacker 攻击方宝可梦
     * @param defender 防御方宝可梦
     * @return 计算出的伤害值
     */
    public int calculateDamage(Pokemon attacker, Pokemon defender) {
        // 基础伤害计算公式: (攻击者攻击力 * 攻击者等级 / 10) - 防御者防御力
        int baseDamage = (attacker.getAttack() * attacker.getLevel() / 10) - defender.getDefense();
        
        // 伤害浮动 (80% - 120%)
        double multiplier = 0.8 + (random.nextDouble() * 0.4);
        
        // 确保伤害最小为1
        int finalDamage = Math.max(1, (int)(baseDamage * multiplier));
        
        return finalDamage;
    }
    
    /**
     * 计算捕获成功率
     * 
     * @param pokemon 要捕获的宝可梦
     * @param ballType 使用的精灵球类型
     * @return 捕获成功率 (0.0 - 1.0)
     */
    public double calculateCaptureRate(Pokemon pokemon, String ballType) {
        // HP越低，捕获率越高
        double hpRate = pokemon.getHp() / (double)pokemon.getMaxHp();
        
        // 等级越高，捕获率越低
        double levelFactor = 1.0 - (pokemon.getLevel() / 100.0);
        
        // 基础成功率
        double baseRate = (1 - hpRate) * levelFactor * 0.75;
        
        // 根据球的类型增加捕获率
        double ballBonus = 1.0;
        switch (ballType) {
            case "great_ball":
                ballBonus = 1.5;
                break;
            case "ultra_ball":
                ballBonus = 2.0;
                break;
            case "master_ball":
                return 1.0; // 必定捕获
            case "net_ball":
                // 假设水和虫系宝可梦捕获更容易
                // 这里简化处理
                ballBonus = 1.5;
                break;
            case "nest_ball":
                // 等级越低捕获率越高
                if (pokemon.getLevel() <= 30) {
                    ballBonus = 2.5 - (pokemon.getLevel() / 15.0);
                }
                break;
        }
        
        // 应用球的加成
        baseRate *= ballBonus;
        
        // 确保捕获率在合理范围内
        return Math.max(0.1, Math.min(0.9, baseRate));
    }
} 