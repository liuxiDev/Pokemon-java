package com.pokemon.game.state;

import com.pokemon.game.entity.Player;
import com.pokemon.game.map.GameMap;
import com.pokemon.game.util.KeyHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * 游戏主状态，玩家在地图上移动
 */
public class PlayState extends GameState {
    
    private Player player;
    private GameMap gameMap;
    private Random random;
    
    // 遇到宝可梦的几率 (1/encounterRate)
    private final int encounterRate = 10;
    
    public PlayState(GameStateManager gsm) {
        super(gsm);
        random = new Random();
    }
    
    @Override
    public void init() {
        player = new Player(400, 300);
        gameMap = new GameMap("town");
    }
    
    @Override
    public void update() {
        // 处理玩家移动
        handlePlayerMovement();
        
        // 更新玩家动画
        player.update();
        
        // 检查是否遇到宝可梦
        checkPokemonEncounter();
        
        // 检查ESC按键返回菜单
        if (KeyHandler.ESC) {
            gsm.setState(GameStateManager.MENU_STATE);
        }
    }
    
    private void handlePlayerMovement() {
        if(KeyHandler.UP) {
            player.moveUp();
        }
        if(KeyHandler.DOWN) {
            player.moveDown();
        }
        if(KeyHandler.LEFT) {
            player.moveLeft();
        }
        if(KeyHandler.RIGHT) {
            player.moveRight();
        }
        
        // 检查碰撞
        gameMap.checkCollision(player);
    }
    
    private void checkPokemonEncounter() {
        // 玩家在草地上有几率遇到宝可梦
        if(gameMap.isOnGrass(player) && player.isMoving()) {
            if(random.nextInt(encounterRate) == 0) {
                // 切换到战斗状态
                gsm.setState(GameStateManager.BATTLE_STATE);
            }
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        // 绘制地图
        gameMap.render(g);
        
        // 绘制玩家
        player.render(g);
        
        // 绘制界面信息
        g.setColor(Color.WHITE);
        g.drawString("真心镇", 20, 20);
        g.drawString("按ESC返回菜单", 650, 20);
    }
} 