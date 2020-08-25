package com.paladin.organization.service.util;

import com.paladin.framework.exception.BusinessException;
import com.paladin.organization.model.DynamicModel;
import com.paladin.organization.model.DynamicProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/4/7
 */
public class DynamicPropertyUtil {


    /**
     * 检查动态模型的属性，并返回检查合格有效的属性
     *
     * @param properties   待检查属性
     * @param dynamicModel 目标动态模型
     * @return 有效属性
     */
    public static Map<String, Object> checkEffectiveProperties(Map<String, Object> properties, DynamicModel dynamicModel) {
        if (dynamicModel == null) {
            throw new BusinessException("动态模型不存在");
        }
        Map<String, Object> effectProperties = null;
        List<DynamicProperty> dynamicProperties = dynamicModel.getProperties();
        if (dynamicProperties != null) {
            effectProperties = new HashMap<>();
            for (DynamicProperty dynamicProperty : dynamicProperties) {
                String code = dynamicProperty.getCode();
                Object value = properties == null ? null : properties.get(code);
                if (value == null || "".equals(value)) {
                    String defaultValue = dynamicProperty.getDefaultValue();
                    if (defaultValue == null || defaultValue.length() == 0) {
                        if (!dynamicProperty.isNullable()) {
                            throw new BusinessException("属性[" + code + "]的值不能为空");
                        }
                    } else {
                        effectProperties.put(code, defaultValue);
                    }
                } else {
                    effectProperties.put(code, value);
                }
            }
        }
        return effectProperties;
    }
}
