package com.hj.core.converters;

import com.hj.core.enums.WaveMarkPosition;

/**
 *
 * convert different position class
 */
public interface PositionConvert<T> {
    T convert(WaveMarkPosition position);
}
