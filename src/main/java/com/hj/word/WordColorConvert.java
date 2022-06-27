package com.hj.word;

import com.hj.core.converters.ColorConvert;
import com.hj.core.enums.WaveMarkColor;

import java.awt.*;

public class WordColorConvert implements ColorConvert<Color> {

    @Override
    public Color convert(WaveMarkColor color) {
        Color wordColor = Color.black; //默认黑色
        switch (color) {
            case BLACK:
                wordColor = Color.black;
                break;
            case RED:
                wordColor = Color.red;
                break;
            case GREEN:
                wordColor = Color.green;
                break;
        }
        return wordColor;
    }
}
