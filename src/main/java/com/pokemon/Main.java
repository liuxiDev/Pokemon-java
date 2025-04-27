package com.pokemon;

import com.pokemon.game.GameFrame;
import com.pokemon.game.util.ImageCreator;

/**
 * 口袋妖怪绿宝石游戏主入口
 */
public class Main {
    public static void main(String[] args) {
        // 首先创建必要的图片资源
        System.out.println("正在初始化游戏资源...");
//        ImageCreator.createMissingImages();
        
        // 启动游戏
        System.out.println("启动游戏...");
        GameFrame gameFrame = new GameFrame();
        gameFrame.start();
    }
} 