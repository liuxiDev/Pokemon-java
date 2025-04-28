package com.pokemon.game.map;

import com.pokemon.game.entity.Player;
import com.pokemon.game.util.ImageLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏地图类，负责管理和渲染游戏地图
 */
public class GameMap {
    
    private String mapName;
    private int[][] mapData;  // 0:道路, 1:墙壁, 2:草地, 3:建筑
    
    // 地图尺寸
    private final int TILE_SIZE = 32;
    private int mapWidth;
    private int mapHeight;
    
    // 碰撞检测用的障碍物列表
    private List<Rectangle> obstacles;
    
    // 草地区域列表
    private List<Rectangle> grassAreas;
    
    // 地图图片
    private BufferedImage mapImage;
    
    /**
     * 创建一个新地图
     * 
     * @param mapName 地图名称
     */
    public GameMap(String mapName) {
        this.mapName = mapName;
        loadMapImage();
        initMap();
        loadObstacles();
    }
    
    /**
     * 加载地图图片
     */
    private void loadMapImage() {
        // 根据地图名称加载对应的地图图片
        String imagePath = "image/map/";
        
        // 默认使用真心镇的地图
        String mapFileName = "wei_bai_zhen.png";
        
        // 根据地图名称选择对应的图片文件
        switch (mapName.toLowerCase()) {
            case "town":
            case "真心镇":
                mapFileName = "wei_bai_zhen.png";
                break;
            case "古辰镇":
                mapFileName = "gu_chen_zhen.png";
                break;
            case "教授家":
                mapFileName = "professor_home.png";
                break;
            case "小智家":
                mapFileName = "boy_player_home.png";
                break;
            case "小智家二楼":
                mapFileName = "boy_player_home_2nd_floor.png";
                break;
            case "城华森林":
                mapFileName = "cheng_hua_forest.png";
                break;
            case "城华市":
                mapFileName = "cheng_hua_shi.png";
                break;
            case "104号道路":
                mapFileName = "Road_104.png";
                break;
            case "103号道路":
                mapFileName = "Road_103.png";
                break;
            case "102号道路":
                mapFileName = "Road_102.png";
                break;
            case "101号道路":
                mapFileName = "Road_101.png";
                break;
        }
        
        // 加载地图图片（路径不要以/开头）
        mapImage = ImageLoader.loadImage(imagePath + mapFileName);
        
        if (mapImage == null) {
            System.err.println("警告：无法加载地图图片: " + mapFileName);
            // 创建一个简单的占位地图
            mapImage = createDefaultMap();
        }
        
        // 设置地图尺寸
        mapWidth = mapImage.getWidth();
        mapHeight = mapImage.getHeight();
    }
    
    /**
     * 创建默认地图作为占位符
     */
    private BufferedImage createDefaultMap() {
        BufferedImage image = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        // 绘制网格背景
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(new Color(200, 255, 200)); // 浅绿色
                } else {
                    g.setColor(new Color(150, 220, 150)); // 深绿色
                }
                g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                
                // 绘制网格线
                g.setColor(Color.DARK_GRAY);
                g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        
        // 添加一些简单的地图元素
        // 中央放置一个房子
        g.setColor(new Color(150, 100, 50)); // 棕色
        g.fillRect(mapWidth/2 * TILE_SIZE - TILE_SIZE*2, mapHeight/2 * TILE_SIZE - TILE_SIZE*2, TILE_SIZE*4, TILE_SIZE*4);
        g.setColor(Color.RED);
        g.fillRect(mapWidth/2 * TILE_SIZE - TILE_SIZE, mapHeight/2 * TILE_SIZE + TILE_SIZE, TILE_SIZE*2, TILE_SIZE/2);
        
        // 添加提示文字
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("默认地图: " + mapName, TILE_SIZE*3, TILE_SIZE*2);
        
        g.dispose();
        return image;
    }
    
    /**
     * 初始化地图数据
     */
    private void initMap() {
        // 根据地图图片大小初始化地图数据
        int numTilesX = (mapWidth + TILE_SIZE - 1) / TILE_SIZE; // 向上取整
        int numTilesY = (mapHeight + TILE_SIZE - 1) / TILE_SIZE; // 向上取整
        
        mapData = new int[numTilesY][numTilesX];
        
        // 初始化地图为道路
        for(int i = 0; i < numTilesY; i++) {
            for(int j = 0; j < numTilesX; j++) {
                mapData[i][j] = 0; // 道路
            }
        }
        
        // 外围墙壁
        for(int i = 0; i < numTilesY; i++) {
            for(int j = 0; j < numTilesX; j++) {
                if(i == 0 || i == numTilesY - 1 || j == 0 || j == numTilesX - 1) {
                    mapData[i][j] = 1; // 墙壁
                }
            }
        }
        
        // 根据图片中添加左侧和右侧房屋的墙壁
        // 左侧房屋
        int leftHouseStartX = 2;
        int leftHouseStartY = 2;
        int houseWidth = 5;
        int houseHeight = 4;
        
        // 右侧房屋
        int rightHouseStartX = numTilesX - 7;
        int rightHouseStartY = 2;
        
        // 添加房屋墙壁
        for(int i = leftHouseStartY; i < leftHouseStartY + houseHeight; i++) {
            for(int j = leftHouseStartX; j < leftHouseStartX + houseWidth; j++) {
                mapData[i][j] = 3; // 建筑
            }
        }
        
        for(int i = rightHouseStartY; i < rightHouseStartY + houseHeight; i++) {
            for(int j = rightHouseStartX; j < rightHouseStartX + houseWidth; j++) {
                mapData[i][j] = 3; // 建筑
            }
        }
        
        // 添加中间建筑
        int middleBuildingStartX = numTilesX / 2 - 2;
        int middleBuildingStartY = numTilesY / 2 - 2;
        int middleBuildingWidth = 4;
        int middleBuildingHeight = 4;
        
        for(int i = middleBuildingStartY; i < middleBuildingStartY + middleBuildingHeight; i++) {
            for(int j = middleBuildingStartX; j < middleBuildingStartX + middleBuildingWidth; j++) {
                mapData[i][j] = 3; // 建筑
            }
        }
        
        // 添加下方建筑
        int bottomBuildingStartX = numTilesX / 2 - 3;
        int bottomBuildingStartY = numTilesY - 6;
        int bottomBuildingWidth = 6;
        int bottomBuildingHeight = 3;
        
        for(int i = bottomBuildingStartY; i < bottomBuildingStartY + bottomBuildingHeight; i++) {
            for(int j = bottomBuildingStartX; j < bottomBuildingStartX + bottomBuildingWidth; j++) {
                mapData[i][j] = 3; // 建筑
            }
        }
        
        // 添加草地区域
        // 左上角草地
        for(int i = 5; i < 10; i++) {
            for(int j = 3; j < 7; j++) {
                mapData[i][j] = 2; // 草地
            }
        }
        
        // 右上角草地
        for(int i = 5; i < 10; i++) {
            for(int j = numTilesX - 8; j < numTilesX - 3; j++) {
                mapData[i][j] = 2; // 草地
            }
        }
        
        // 右下角草地
        for(int i = numTilesY - 10; i < numTilesY - 5; i++) {
            for(int j = numTilesX - 8; j < numTilesX - 3; j++) {
                mapData[i][j] = 2; // 草地
            }
        }
        
        // 左下角草地
        for(int i = numTilesY - 10; i < numTilesY - 5; i++) {
            for(int j = 3; j < 7; j++) {
                mapData[i][j] = 2; // 草地
            }
        }
    }
    
    /**
     * 加载障碍物和草地区域
     */
    private void loadObstacles() {
        obstacles = new ArrayList<>();
        grassAreas = new ArrayList<>();
        
        for(int i = 0; i < mapData.length; i++) {
            for(int j = 0; j < mapData[i].length; j++) {
                if(mapData[i][j] == 1 || mapData[i][j] == 3) {
                    // 墙壁和建筑是障碍物
                    obstacles.add(new Rectangle(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                } else if(mapData[i][j] == 2) {
                    // 草地区域
                    grassAreas.add(new Rectangle(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                }
            }
        }
    }
    
    /**
     * 检查玩家与障碍物的碰撞
     * 
     * @param player 玩家对象
     */
    public void checkCollision(Player player) {
        // 设置玩家不能超出地图边界
        player.setMapBounds(mapWidth, mapHeight);
        
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        
        for(Rectangle obstacle : obstacles) {
            if(playerRect.intersects(obstacle)) {
                // 发生碰撞，根据当前速度的反方向移动玩家
                if(player.getX() + player.getWidth() > obstacle.x && player.getX() < obstacle.x) {
                    player.setX(obstacle.x - player.getWidth());
                } else if(player.getX() < obstacle.x + obstacle.width && player.getX() + player.getWidth() > obstacle.x + obstacle.width) {
                    player.setX(obstacle.x + obstacle.width);
                }
                
                if(player.getY() + player.getHeight() > obstacle.y && player.getY() < obstacle.y) {
                    player.setY(obstacle.y - player.getHeight());
                } else if(player.getY() < obstacle.y + obstacle.height && player.getY() + player.getHeight() > obstacle.y + obstacle.height) {
                    player.setY(obstacle.y + obstacle.height);
                }
                
                player.setMoving(false);
            }
        }
    }
    
    /**
     * 检查玩家是否在草地上
     * 
     * @param player 玩家对象
     * @return 是否在草地上
     */
    public boolean isOnGrass(Player player) {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        
        for(Rectangle grass : grassAreas) {
            if(playerRect.intersects(grass)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 渲染地图
     * 
     * @param g 图形对象
     */
    public void render(Graphics2D g) {
        // 绘制地图图片
        g.drawImage(mapImage, 0, 0, null);
        
        // 如果需要调试碰撞区域，可以绘制网格和碰撞框
         renderDebugGrid(g);
    }
    
    /**
     * 渲染调试网格（仅用于开发调试）
     * 
     * @param g 图形对象
     */
    private void renderDebugGrid(Graphics2D g) {
        for(int i = 0; i < mapData.length; i++) {
            for(int j = 0; j < mapData[i].length; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                
                // 绘制网格线
                g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                
                // 根据地图类型绘制调试颜色
                switch(mapData[i][j]) {
                    case 1: // 墙壁
                        g.drawString("墙", x + 10, y + 20);
                        break;
                    case 2: // 草地
                        g.drawString("草", x + 10, y + 20);
                        break;
                    case 3: // 建筑
                        g.drawString("建", x + 10, y + 20);
                        break;
                }
            }
        }
        
        // 绘制障碍物
        for(Rectangle obstacle : obstacles) {
            g.drawRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
        
        // 绘制草地区域
        for(Rectangle grass : grassAreas) {
            g.drawRect(grass.x, grass.y, grass.width, grass.height);
        }
    }
    
    /**
     * 获取地图宽度
     */
    public int getMapWidth() {
        return mapWidth;
    }
    
    /**
     * 获取地图高度
     */
    public int getMapHeight() {
        return mapHeight;
    }
} 