package com.pokemon.game.state;

import com.pokemon.game.battle.BattleSystem;
import com.pokemon.game.entity.Pokemon;
import com.pokemon.game.util.ImageLoader;
import com.pokemon.game.util.KeyHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 宝可梦战斗状态
 */
public class BattleState extends GameState {
    
    private enum BattlePhase {
        START, PLAYER_TURN, ENEMY_TURN, CAPTURE, WIN, LOSE, END
    }
    
    private BattlePhase currentPhase;
    private int currentChoice = 0;
    private String[] options = {"战斗", "捕捉", "道具", "逃跑"};
    
    private Pokemon playerPokemon; // 皮卡丘
    private Pokemon enemyPokemon;  // 随机敌方宝可梦
    private BattleSystem battleSystem;
    
    private String battleMessage = "";
    private int messageTimer = 0;
    
    // 战斗界面背景
    private BufferedImage battleBackground;
    
    // 精灵球动画相关
    private String currentBallType = "poke_ball";
    private BufferedImage pokeBallImage;
    private int ballX, ballY;
    private int ballAnimationStep = 0;
    private boolean showBallAnimation = false;
    
    public BattleState(GameStateManager gsm) {
        super(gsm);
        battleSystem = new BattleSystem();
        loadImages();
    }
    
    /**
     * 加载图像资源
     */
    private void loadImages() {
        // 尝试加载战斗背景图片
        // 注意：我们使用正确的路径格式，不要以/开头
        battleBackground = ImageLoader.loadImage("image/battle/background.png");
        
        // 如果无法加载战斗背景，创建一个默认的
        if (battleBackground == null) {
            battleBackground = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = battleBackground.createGraphics();
            g.setColor(new Color(200, 255, 200)); // 浅绿色背景
            g.fillRect(0, 0, 800, 600);
            g.dispose();
        }
    }
    
    @Override
    public void init() {
        // 初始化玩家的皮卡丘
        playerPokemon = new Pokemon("皮卡丘", 25, 100, 10, 5);
        
        // 随机生成敌方宝可梦
        String[] pokemonNames = {"小火龙", "杰尼龟", "妙蛙种子", "可达鸭", "喵喵"};
        Random random = new Random();
        String enemyName = pokemonNames[random.nextInt(pokemonNames.length)];
        int enemyLevel = random.nextInt(10) + 10; // 10-20级
        
        enemyPokemon = new Pokemon(enemyName, enemyLevel, 80, 8, 3);
        
        currentPhase = BattlePhase.START;
        battleMessage = "野生的" + enemyPokemon.getName() + "出现了！";
        messageTimer = 60; // 约1秒
        
        // 获取精灵球图片
        pokeBallImage = battleSystem.getBallImage(currentBallType);
    }
    
    @Override
    public void update() {
        if(messageTimer > 0) {
            messageTimer--;
            if(messageTimer == 0 && currentPhase == BattlePhase.START) {
                currentPhase = BattlePhase.PLAYER_TURN;
                battleMessage = "请选择行动！";
            }
            return;
        }
        
        // 更新精灵球动画
        if (showBallAnimation) {
            updateBallAnimation();
            return;
        }
        
        switch(currentPhase) {
            case PLAYER_TURN:
                handlePlayerTurn();
                break;
            case ENEMY_TURN:
                enemyAction();
                break;
            case CAPTURE:
                attemptCapture();
                break;
            case WIN:
            case LOSE:
                if(KeyHandler.ENTER) {
                    gsm.setState(GameStateManager.PLAY_STATE);
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 更新精灵球动画
     */
    private void updateBallAnimation() {
        ballAnimationStep++;
        
        // 简单的抛物线轨迹
        if (ballAnimationStep < 30) {
            ballX += 10;
            ballY = 350 - (int)(20 * Math.sin(ballAnimationStep * Math.PI / 30));
        } else if (ballAnimationStep < 40) {
            // 到达目标位置后摇晃
        } else if (ballAnimationStep < 60) {
            // 决定捕获结果
            if (ballAnimationStep == 40) {
                double captureRate = battleSystem.calculateCaptureRate(enemyPokemon, currentBallType);
                boolean success = Math.random() < captureRate;
                
                if (success) {
                    battleMessage = "恭喜！捕获了" + enemyPokemon.getName() + "！";
                    currentPhase = BattlePhase.WIN;
                } else {
                    battleMessage = enemyPokemon.getName() + "挣脱了出来！";
                    currentPhase = BattlePhase.ENEMY_TURN;
                }
            }
        } else {
            // 结束动画
            showBallAnimation = false;
            messageTimer = 60;
        }
    }
    
    private void handlePlayerTurn() {
        // 选择菜单
        if(KeyHandler.UP && currentChoice > 0) {
            currentChoice--;
        }
        if(KeyHandler.DOWN && currentChoice < options.length - 1) {
            currentChoice++;
        }
        
        if(KeyHandler.ENTER) {
            switch(currentChoice) {
                case 0: // 战斗
                    battleMessage = playerPokemon.getName() + "使用了电击！";
                    int damage = battleSystem.calculateDamage(playerPokemon, enemyPokemon);
                    enemyPokemon.takeDamage(damage);
                    battleMessage += "造成了" + damage + "点伤害！";
                    messageTimer = 60;
                    
                    if(enemyPokemon.getHp() <= 0) {
                        currentPhase = BattlePhase.WIN;
                        battleMessage = "你打败了" + enemyPokemon.getName() + "！";
                        messageTimer = 120;
                    } else {
                        currentPhase = BattlePhase.ENEMY_TURN;
                    }
                    break;
                case 1: // 捕捉
                    startBallAnimation();
                    battleMessage = "你投出了精灵球！";
                    break;
                case 2: // 道具
                    battleMessage = "你使用了药水，恢复了一些HP！";
                    playerPokemon.heal(20);
                    messageTimer = 60;
                    currentPhase = BattlePhase.ENEMY_TURN;
                    break;
                case 3: // 逃跑
                    battleMessage = "成功逃跑！";
                    messageTimer = 60;
                    currentPhase = BattlePhase.END;
                    break;
            }
        }
    }
    
    /**
     * 开始精灵球动画
     */
    private void startBallAnimation() {
        showBallAnimation = true;
        ballAnimationStep = 0;
        ballX = 200; // 起始X位置
        ballY = 350; // 起始Y位置
        
        // 获取当前使用的精灵球
        currentBallType = "poke_ball"; // 默认使用普通精灵球
        pokeBallImage = battleSystem.getBallImage(currentBallType);
    }
    
    private void enemyAction() {
        if(messageTimer == 0) {
            battleMessage = enemyPokemon.getName() + "发起了攻击！";
            int damage = battleSystem.calculateDamage(enemyPokemon, playerPokemon);
            playerPokemon.takeDamage(damage);
            battleMessage += "造成了" + damage + "点伤害！";
            messageTimer = 60;
            
            if(playerPokemon.getHp() <= 0) {
                currentPhase = BattlePhase.LOSE;
                battleMessage = "皮卡丘失去了战斗能力！";
                messageTimer = 120;
            } else {
                currentPhase = BattlePhase.PLAYER_TURN;
            }
        }
    }
    
    private void attemptCapture() {
        startBallAnimation();
    }
    
    @Override
    public void render(Graphics2D g) {
        // 绘制战斗背景
        g.drawImage(battleBackground, 0, 0, null);
        
        // 绘制敌方宝可梦
        enemyPokemon.render(g, 600, 150, true);
        
        // 绘制玩家宝可梦
        playerPokemon.render(g, 200, 300, false);
        
        // 绘制HP条
        drawHealthBar(g, enemyPokemon, 580, 150);
        drawHealthBar(g, playerPokemon, 180, 300);
        
        // 绘制战斗信息框
        g.setColor(Color.WHITE);
        g.fillRect(0, 450, 800, 150);
        g.setColor(Color.BLACK);
        g.drawRect(0, 450, 800, 150);
        
        // 绘制战斗信息
        g.setFont(new Font("黑体", Font.PLAIN, 20));
        g.drawString(battleMessage, 20, 480);
        
        // 如果是玩家的回合，绘制选项
        if(currentPhase == BattlePhase.PLAYER_TURN && messageTimer == 0 && !showBallAnimation) {
            g.setFont(new Font("黑体", Font.PLAIN, 18));
            for(int i = 0; i < options.length; i++) {
                if(i == currentChoice) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.drawString(options[i], 500, 480 + i * 30);
            }
        }
        
        // 如果有精灵球动画，绘制精灵球
        if (showBallAnimation && pokeBallImage != null) {
            g.drawImage(pokeBallImage, ballX, ballY, null);
        }
    }
    
    private void drawHealthBar(Graphics2D g, Pokemon pokemon, int x, int y) {
        int width = 150;
        int height = 10;
        
        // 绘制背景
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        
        // 计算健康百分比
        double healthPercent = pokemon.getHp() / (double)pokemon.getMaxHp();
        int healthWidth = (int)(width * healthPercent);
        
        // 根据健康值设置颜色
        if(healthPercent > 0.5) {
            g.setColor(Color.GREEN);
        } else if(healthPercent > 0.2) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        
        // 绘制健康条
        g.fillRect(x, y, healthWidth, height);
        
        // 绘制健康值文本
        g.setColor(Color.BLACK);
        g.setFont(new Font("黑体", Font.PLAIN, 12));
        g.drawString(pokemon.getHp() + "/" + pokemon.getMaxHp(), x + width + 5, y + 10);
        
        // 绘制宝可梦名称和等级
        g.setFont(new Font("黑体", Font.PLAIN, 14));
        g.drawString(pokemon.getName() + " Lv." + pokemon.getLevel(), x, y - 5);
    }
} 