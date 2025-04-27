package com.pokemon.game.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 键盘输入处理器
 */
public class KeyHandler implements KeyListener {
    
    // 键盘状态
    public static boolean UP, DOWN, LEFT, RIGHT, ENTER, SPACE, ESC;
    
    // 上一帧键盘状态
    private boolean[] previousKeys = new boolean[256];
    // 当前帧键盘状态
    private boolean[] currentKeys = new boolean[256];
    
    public KeyHandler() {
        UP = DOWN = LEFT = RIGHT = ENTER = SPACE = ESC = false;
    }
    
    public void update() {
        System.arraycopy(currentKeys, 0, previousKeys, 0, currentKeys.length);
    }
    
    // 检查按键是否刚被按下
    public boolean isPressed(int keyCode) {
        return currentKeys[keyCode] && !previousKeys[keyCode];
    }
    
    // 检查按键是否正在被按下
    public boolean isDown(int keyCode) {
        return currentKeys[keyCode];
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // 不需要实现
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        currentKeys[e.getKeyCode()] = true;
        
        // 更新方向键状态
        if(e.getKeyCode() == KeyEvent.VK_UP) UP = true;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) DOWN = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT) LEFT = true;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) RIGHT = true;
        if(e.getKeyCode() == KeyEvent.VK_ENTER) ENTER = true;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) SPACE = true;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) ESC = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        currentKeys[e.getKeyCode()] = false;
        
        // 更新方向键状态
        if(e.getKeyCode() == KeyEvent.VK_UP) UP = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) DOWN = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT) LEFT = false;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) RIGHT = false;
        if(e.getKeyCode() == KeyEvent.VK_ENTER) ENTER = false;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) SPACE = false;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) ESC = false;
    }
} 