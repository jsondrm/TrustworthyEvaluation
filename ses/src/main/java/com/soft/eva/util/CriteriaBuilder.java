package com.soft.eva.util;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.regex.Pattern;

/**
 * Criteria构建器
 */
public class CriteriaBuilder {

    /**
     * 构建is的Criteria对象
     *
     * @param field 字段名
     * @param value 字段值
     * @return Criteria对象
     */
    public static Criteria is(String field, Object value) {
        if (value == null) {
            return new Criteria();
        }
        return Criteria.where(field).is(value);
    }

    /**
     * 构造exist的Criteria对象
     *
     * @param field 字段
     * @param flag  是否存在
     * @return Criteria对象
     */
    public static Criteria exist(String field, boolean flag) {
        return Criteria.where(field).exists(flag);
    }

    /**
     * 构建in的Criteria对象
     *
     * @param field  字段名
     * @param values in的值范围
     * @return Criteria对象
     */
    public static Criteria in(String field, Object... values) {
        return Criteria.where(field).in(values);
    }

    /**
     * 构建between的Criteria对象
     *
     * @param field    字段名
     * @param minValue 最小值
     * @param maxValue 最大值
     * @return Criteria对象
     */
    public static Criteria between(String field, Object minValue, Object maxValue) {
        if (minValue == null && maxValue == null) {
            return new Criteria();
        }
        if (minValue != null && maxValue != null) {
            return Criteria.where(field).lte(maxValue).gte(minValue);
        }
        if (minValue != null) {
            return Criteria.where(field).gte(minValue);
        }
        return Criteria.where(field).lte(maxValue);
    }

    /**
     * 构建模糊匹配的Criteria对象：区分大小写
     *
     * @param field 字段名
     * @param value 字段值
     * @return Criteria对象
     */
    public static Criteria fuzzyMatch(String field, Object value) {
        if (value == null) {
            return new Criteria();
        }
        Pattern pattern = Pattern.compile(filterPattern(value.toString()));
        return Criteria.where(field).regex(pattern);
    }

    /**
     * 构建模糊匹配的Criteria对象：不区分大小写
     *
     * @param field 字段名
     * @param value 字段值
     * @return Criteria对象
     */
    public static Criteria fuzzyMatchIgnoreCase(String field, Object value) {
        if (value == null) {
            return new Criteria();
        }
        Pattern pattern = Pattern.compile(filterPattern(value.toString()), Pattern.CASE_INSENSITIVE);
        return Criteria.where(field).regex(pattern);
    }

    /**
     * 拼接Criteria对象,且关系
     *
     * @param criterion Criteria对象数组
     * @return 拼接后的Criteria对象
     */
    public static Criteria andCriteriaOperator(Criteria... criterion) {
        Criteria criteria = new Criteria();
        return criteria.andOperator(criterion);
    }

    /**
     * 拼接Criteria对象，or关系
     *
     * @param criterion Criteria对象数组
     * @return 拼接后的Criteria对象
     */
    public static Criteria orCriteriaOperator(Criteria... criterion) {
        Criteria criteria = new Criteria();
        return criteria.orOperator(criterion);
    }

    /**
     * 转义字符处理
     *
     * @param pattern
     * @return 处理后的字符串
     */
    public static String filterPattern(String pattern) {
        return pattern.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }
}
