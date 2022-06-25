package com.hj.core.converters;

import com.hj.core.enums.WaveMarkColor;

/**
 * convert different color class
 */
public interface ColorConvert<T> {
    T convert(WaveMarkColor color);
}
