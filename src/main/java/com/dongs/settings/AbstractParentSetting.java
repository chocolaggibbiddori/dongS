package com.dongs.settings;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

abstract class AbstractParentSetting {

    void init() {
        Class<? extends AbstractParentSetting> clazz = getClass();
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) continue;

            Class<?> fieldType = field.getType();
            field.setAccessible(true);

            Object o;
            try {
                o = field.get(this);
            } catch (IllegalAccessException e) {
                return;
            }

            Class<AbstractParentSetting> parentSettingClass = AbstractParentSetting.class;
            if (parentSettingClass.isAssignableFrom(fieldType)) {
                AbstractParentSetting parentSetting = parentSettingClass.cast(o);
                parentSetting.init();
            } else {
                DefaultValue defaultValue = (DefaultValue) o;
                Object defaultObject = defaultValue.getDefault();

                try {
                    field.set(this, defaultObject);
                } catch (IllegalAccessException e) {
                    return;
                }
            }
        }
    }
}
