package com.pokemon.game.state;

import java.awt.Graphics2D;

/**
 * 游戏状态基类，所有具体游戏状态都继承自此类
 */
public abstract class GameState {
    
    protected GameStateManager gsm;
    
    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }
    
    // 初始化状态
    public abstract void init();
    
    // 更新状态
    public abstract void update();
    
    // 渲染状态
    public abstract void render(Graphics2D g);
} 