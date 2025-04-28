package com.pokemon.game.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * 图像资源加载工具类
 */
public class ImageLoader {
    
    // 图片缓存，避免重复加载
    private static Map<String, BufferedImage> imageCache = new HashMap<>();
    
    /**
     * 加载图片资源
     * 
     * @param path 图片路径
     * @return 缓冲图像对象
     */
    public static BufferedImage loadImage(String path) {
        // 检查缓存
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        
        try {
            // 确保路径格式正确
            String resourcePath = path;
            if (resourcePath.startsWith("/")) {
                resourcePath = resourcePath.substring(1);
            }
            
            // 首先尝试从文件系统加载
            File file = new File("src/main/resources/" + resourcePath);
            if (file.exists()) {
                System.out.println("从文件系统加载图片: " + file.getAbsolutePath());
                BufferedImage image = ImageIO.read(file);
                imageCache.put(path, image);
                return image;
            }
            
            // 如果文件系统加载失败，尝试从资源加载
            String classPath = "/" + resourcePath;
            InputStream stream = ImageLoader.class.getResourceAsStream(classPath);
            
            if (stream != null) {
                System.out.println("从资源加载图片: " + classPath);
                BufferedImage image = ImageIO.read(stream);
                imageCache.put(path, image);
                return image;
            } else {
                System.err.println("图片资源不存在: " + path);
                // 创建一个简单的占位图
                return createPlaceholderImage(path);
            }
        } catch (IOException e) {
            System.err.println("无法加载图片: " + path);
            e.printStackTrace();
            return createPlaceholderImage(path);
        }
    }
    
    /**
     * 创建占位图像，当图片加载失败时使用
     * 
     * @param imageName 图片名称，用于生成颜色
     * @return 占位图像
     */
    private static BufferedImage createPlaceholderImage(String imageName) {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        // 使用路径名生成不同颜色
        int hash = imageName.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g2 = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        
        // 绘制占位图
        g.setColor(new java.awt.Color(r, g2, b));
        g.fillRect(0, 0, 64, 64);
        g.setColor(java.awt.Color.WHITE);
        g.drawRect(2, 2, 60, 60);
        
        String fileName = imageName.substring(imageName.lastIndexOf('/') + 1);
        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 8) + "...";
        }
        
        g.drawString(fileName, 5, 32);
        g.dispose();
        
        return image;
    }
    
    /**
     * 将Image转换为BufferedImage
     * 
     * @param image 原图像
     * @return 缓冲图像
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        
        BufferedImage buffered = new BufferedImage(
            image.getWidth(null), 
            image.getHeight(null), 
            BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g = buffered.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        return buffered;
    }
    
    /**
     * 获取子图像（用于精灵表中提取单个图像）
     * 
     * @param spriteSheet 精灵表
     * @param x 起始x坐标
     * @param y 起始y坐标
     * @param width 宽度
     * @param height 高度
     * @return 子图像
     */
    public static BufferedImage getSubImage(BufferedImage spriteSheet, int x, int y, int width, int height) {
        if (spriteSheet == null) {
            System.err.println("精灵表为空，无法获取子图像");
            return createPlaceholderImage("subimage");
        }
        
        // 确保索引在图像范围内
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > spriteSheet.getWidth()) width = spriteSheet.getWidth() - x;
        if (y + height > spriteSheet.getHeight()) height = spriteSheet.getHeight() - y;
        
        // 如果尺寸不合法，返回占位图像
        if (width <= 0 || height <= 0) {
            System.err.println("子图像尺寸无效: " + width + "x" + height);
            return createPlaceholderImage("subimage_error");
        }
        
        return spriteSheet.getSubimage(x, y, width, height);
    }
} 