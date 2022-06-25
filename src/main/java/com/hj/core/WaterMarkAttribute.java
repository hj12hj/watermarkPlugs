package com.hj.core;

import com.hj.core.enums.WaveMarkColor;

/**
 * 水印属性
 */
public class WaterMarkAttribute {

    /**
     * 字体间距
     */
    Float characterSpacing;

    /**
     * 水印颜色
     */
    WaveMarkColor color;

    /**
     * 透明度
     */
    Float opacity;

    /**
     * 字体大小
     */
    Float fontSize;

    /**
     * 水印位置
     */
    Integer position;

    /**
     * 距离x 距离
     */
    Float x;

    /**
     * 距离y 距离
     */
    Float y;

    /**
     * 旋转角度
     */
    Float rotation;


    public Float getCharacterSpacing() {
        return characterSpacing;
    }

    public void setCharacterSpacing(Float characterSpacing) {
        this.characterSpacing = characterSpacing;
    }

    public WaveMarkColor getColor() {
        return color;
    }

    public void setColor(WaveMarkColor color) {
        this.color = color;
    }

    public Float getOpacity() {
        return opacity;
    }

    public void setOpacity(Float opacity) {
        this.opacity = opacity;
    }

    public Float getFontSize() {
        return fontSize;
    }

    public void setFontSize(Float fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getRotation() {
        return rotation;
    }

    public void setRotation(Float rotation) {
        this.rotation = rotation;
    }
}
