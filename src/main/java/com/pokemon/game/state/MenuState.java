package com.pokemon.game.state;

import com.pokemon.game.util.KeyHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * 游戏主菜单状态
 */
public class MenuState extends GameState {
    
    private String[] options = {"开始游戏", "退出"};
    private int currentChoice = 0;
    
    public MenuState(GameStateManager gsm) {
        super(gsm);
    }
    
    @Override
    public void init() {
        // 初始化菜单
    }
    
    @Override
    public void update() {
        // 处理键盘输入
        if(KeyHandler.UP && currentChoice > 0) {
            currentChoice--;
        }
        if(KeyHandler.DOWN && currentChoice < options.length - 1) {
            currentChoice++;
        }
        if(KeyHandler.ENTER) {
            selectOption();
        }
    }
    
    private void selectOption() {
        if(currentChoice == 0) {
            // 开始游戏
            gsm.setState(GameStateManager.PLAY_STATE);
        } else if(currentChoice == 1) {
            // 退出游戏
            System.exit(0);
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        // 绘制背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 320, 320);
        
        // 绘制标题
        g.setColor(Color.YELLOW);
        g.setFont(new Font("黑体", Font.BOLD, 24));
        g.drawString("口袋妖怪绿宝石", 50, 75);
        
        // 绘制选项
        g.setFont(new Font("黑体", Font.PLAIN, 14));
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options[i], 120, 120 + i * 20);
        }
    }
} 