package com.pokemon.game.state;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * 游戏状态管理器，负责管理不同游戏场景的切换
 */
public class GameStateManager {

    // 游戏状态列表
    private ArrayList<GameState> gameStates;
    private int currentState;
    
    // 游戏状态常量
    public static final int MENU_STATE = 0;
    public static final int PLAY_STATE = 1;
    public static final int BATTLE_STATE = 2;
    
    public GameStateManager() {
        gameStates = new ArrayList<GameState>();
        currentState = MENU_STATE;
        
        gameStates.add(new MenuState(this));
        gameStates.add(new PlayState(this));
        gameStates.add(new BattleState(this));
    }
    
    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }
    
    public void update() {
        gameStates.get(currentState).update();
    }
    
    public void render(Graphics2D g) {
        gameStates.get(currentState).render(g);
    }
} 