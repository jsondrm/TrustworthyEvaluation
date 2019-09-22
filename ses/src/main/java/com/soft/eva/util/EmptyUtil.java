package com.soft.eva.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class  EmptyUtil {

    /**
     * 私有构造
     */
    private EmptyUtil() {
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str == "" || str.trim() == "" || str.length() <= 0;
    }

    /**
     * 判断List是否为空
     *
     * @param list
     * @return true or false
     */
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @return true or false
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     * @return true or false
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return true or false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断List是否不为空
     *
     * @param list
     * @return true or false
     */
    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    /**
     * 判断Map是否不为空
     *
     * @param map
     * @return true or false
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数组是否不为空
     *
     * @param array
     * @return true or false
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断对象是否不为空
     *
     * @param object 要判断的对象
     * @param <T>
     * @return 判断结果
     */
    public static <T> boolean objectIsNotEmpty(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof String) {
            if (object != null && !"".equals(object)) {
                return true;
            }
        } else if (object instanceof Integer || object instanceof Double || object instanceof Float) {
            if (object != null) {
                return true;
            }
        } else if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            if (object != null && array.length != 0) {
                return true;
            }
        } else if (object instanceof Collection) {
            Collection<T> collection = (Collection<T>) object;
            if (collection != null && collection.size() != 0) {
                return true;
            }
        } else if (object instanceof Map) {
            Map<T, T> map = (Map<T, T>) object;
            if (map != null && map.size() != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为空
     *
     * @param object 要判断的对象
     * @return 判断结果
     */
    public static boolean objectIsEmpty(Object object) {
        return !objectIsNotEmpty(object);
    }
}
