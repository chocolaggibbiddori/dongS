package com.dongs.settings;

import com.dongs.settings.interfaces.ValueSetting;

/**
 * @param <T> 구현체 타입
 * @param <V> 값 타입
 */
interface ChildSetting<T, V> extends DefaultValue<T>, ValueSetting<V> {
}
