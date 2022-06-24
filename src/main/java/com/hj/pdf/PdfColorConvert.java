package com.hj.pdf;

import com.hj.core.WaveMarkColor;
import com.hj.core.ColorConvert;
import com.itextpdf.text.BaseColor;

/**
 * pdf 颜色属性转换
 */
public class PdfColorConvert implements ColorConvert<BaseColor> {

    @Override
    public BaseColor convert(WaveMarkColor color) {
        BaseColor baseColor = null;
        switch (color) {
            case WHITE:
                baseColor = BaseColor.WHITE;
                break;
            case LIGHT_GRAY:
                baseColor = BaseColor.LIGHT_GRAY;
                break;
            case GRAY:
                baseColor = BaseColor.GRAY;
                break;
            case DARK_GRAY:
                baseColor = BaseColor.DARK_GRAY;
                break;
            case BLACK:
                baseColor = BaseColor.BLACK;
                break;
            case RED:
                baseColor = BaseColor.RED;
                break;
            case PINK:
                baseColor = BaseColor.PINK;
                break;
            case ORANGE:
                baseColor = BaseColor.ORANGE;
                break;
            case YELLOW:
                baseColor = BaseColor.YELLOW;
                break;
            case GREEN:
                baseColor = BaseColor.GREEN;
                break;
            case MAGENTA:
                baseColor = BaseColor.MAGENTA;
                break;
            case CYAN:
                baseColor = BaseColor.CYAN;
                break;
            case BLUE:
                baseColor = BaseColor.BLUE;
                break;
        }


        return baseColor;
    }
}
