package com.pokemon.game;

import com.pokemon.game.map.GameMap;
import com.pokemon.game.state.GameStateManager;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * 游戏窗口类，继承自JFrame，负责创建游戏窗口和管理游戏循环
 */
public class GameWindow extends JFrame implements Runnable {
    
    // 窗口默认大小
    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 320;
    
    // 窗口当前大小
    private int width;
    private int height;
    
    // 游戏画布
    private Canvas canvas;
    
    // 游戏状态管理器
    private GameStateManager gameStateManager;
    
    // 游戏线程
    private Thread gameThread;
    private boolean running;
    
    // 游戏FPS
    private final int FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / FPS;
    
    // 当前地图
    private GameMap currentMap;
    
    /**
     * 创建一个新的游戏窗口
     */
    public GameWindow() {
        // 设置窗口标题
        super("口袋妖怪 翡翠版");
        
        // 初始化窗口大小
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        
        // 设置窗口大小
        setSize(width, height);
        
        // 设置窗口居中显示
        setLocationRelativeTo(null);
        
        // 设置关闭窗口时退出程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 禁止窗口大小调整
        setResizable(false);
        
        // 创建画布
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        
        // 将画布添加到窗口中
        add(canvas);
        
        // 让窗口大小适应画布
        pack();
        
        // 创建游戏状态管理器
        gameStateManager = new GameStateManager(this);
        
        // 初始化游戏
        init();
        
        // 显示窗口
        setVisible(true);
    }
    
    /**
     * 初始化游戏
     */
    private void init() {
        // 创建游戏线程
        gameThread = new Thread(this);
        
        // 设置线程名称
        gameThread.setName("GameThread");
        
        // 开始运行游戏线程
        running = true;
        gameThread.start();
    }
    
    /**
     * 游戏线程运行方法
     */
    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();
        long lastFpsTime = 0;
        int fps = 0;
        
        // 游戏循环
        while (running) {
            // 计算循环时间
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);
            
            // 更新FPS计数器
            lastFpsTime += updateLength;
            fps++;
            
            // 如果过了1秒，更新FPS并重置计数器
            if (lastFpsTime >= 1000000000) {
                // 设置窗口标题为游戏名和FPS
                setTitle("口袋妖怪 翡翠版 - FPS: " + fps);
                lastFpsTime = 0;
                fps = 0;
            }
            
            // 更新游戏逻辑
            update(delta);
            
            // 渲染游戏画面
            render();
            
            // 等待保持帧率稳定
            try {
                long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 更新游戏逻辑
     * 
     * @param delta 时间增量
     */
    private void update(double delta) {
        // delta
        gameStateManager.update();
    }
    
    /**
     * 渲染游戏画面
     */
    private void render() {
        // 获取画布的缓冲策略
        BufferStrategy bs = canvas.getBufferStrategy();
        
        // 如果缓冲策略为空，创建一个三重缓冲
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        
        // 获取图形对象
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        
        // 清空画布
        g.clearRect(0, 0, width, height);
        
        // 渲染游戏状态
        gameStateManager.render(g);
        
        // 释放图形对象
        g.dispose();
        
        // 显示缓冲
        bs.show();
    }
    
    /**
     * 设置当前地图并根据地图大小调整窗口
     * 
     * @param map 游戏地图
     */
    public void setCurrentMap(GameMap map) {
        this.currentMap = map;
        
        // 根据地图大小调整窗口大小
        if (map != null) {
            // 获取地图尺寸
            int mapWidth = map.getMapWidth();
            int mapHeight = map.getMapHeight();
            
            // 窗口大小不要超过屏幕分辨率的80%
            int maxWidth = (int)(java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getMaximumWindowBounds().getWidth() * 0.8);
            int maxHeight = (int)(java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getMaximumWindowBounds().getHeight() * 0.8);
            
            // 确保窗口大小不小于默认值，也不大于屏幕的80%
            this.width = Math.max(DEFAULT_WIDTH, Math.min(mapWidth, maxWidth));
            this.height = Math.max(DEFAULT_HEIGHT, Math.min(mapHeight, maxHeight));
            
            // 更新画布大小
            canvas.setPreferredSize(new Dimension(width, height));
            canvas.setMaximumSize(new Dimension(width, height));
            canvas.setMinimumSize(new Dimension(width, height));
            
            // 更新窗口大小
            pack();
            setLocationRelativeTo(null); // 窗口居中
        }
    }
    
    /**
     * 获取游戏状态管理器
     * 
     * @return 游戏状态管理器
     */
    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
    
    /**
     * 获取窗口宽度
     * 
     * @return 窗口宽度
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 获取窗口高度
     * 
     * @return 窗口高度
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 获取当前地图
     * 
     * @return 当前地图
     */
    public GameMap getCurrentMap() {
        return currentMap;
    }
    
    /**
     * 停止游戏
     */
    public void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
} 