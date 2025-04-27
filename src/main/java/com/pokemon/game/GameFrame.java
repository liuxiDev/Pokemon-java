package com.pokemon.game;

import com.pokemon.game.state.GameStateManager;
import com.pokemon.game.util.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 游戏主窗口，负责游戏循环和绘制
 */
public class GameFrame extends JFrame implements Runnable {

    // 窗口尺寸
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "口袋妖怪绿宝石";

    // 游戏循环相关
    private Thread gameThread;
    private boolean running = false;
    private final int FPS = 60;
    private final long targetTime = 1000 / FPS;

    // 游戏画布
    private BufferedImage image;
    private Graphics2D g;
    private JPanel panel;

    // 游戏状态管理器
    private GameStateManager gsm;
    private KeyHandler keyHandler;

    public GameFrame() {
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, null);
                }
            }
        };
        
        add(panel);
        
        setVisible(true);
    }

    private void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
        running = true;
        
        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);
        
        gsm = new GameStateManager(null);
    }

    public void start() {
        init();
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long startTime;
        long elapsedTime;
        long waitTime;

        // 游戏主循环
        while (running) {
            startTime = System.nanoTime();
            
            update();
            render();
            drawToScreen();
            
            elapsedTime = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - elapsedTime;
            
            if (waitTime < 0) {
                waitTime = 5;
            }
            
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        gsm.update();
        keyHandler.update();
    }

    private void render() {
        // 清空画布
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制游戏状态
        gsm.render(g);
    }

    private void drawToScreen() {
        panel.repaint();
    }
} 