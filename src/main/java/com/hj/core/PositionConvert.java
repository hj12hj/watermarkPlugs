package com.hj.core;

/**
 *
 * convert different position class
 */
public interface PositionConvert<T> {
    T convert(WaveMarkPosition position);
}
