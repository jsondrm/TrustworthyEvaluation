package com.soft.eva.util;

/**
 * 用层次分析法计算权重
 */
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AHP {
        // 单例
        private static final AHP cwbp = new AHP();

        // 平均随机一致性指针
        private double[] RI = { 0.00, 0.00, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41,
                1.45, 1.49 };

        // 随机一致性比率
        private double CR = 0.0;

        // 最大特征值
        private double lamta = 0.0;

        /**
         * 私有构造
         */
        public AHP() {

        }

        /**
         * 返回单例
         *
         * @return
         */
        public static AHP getInstance() {
            return cwbp;
        }

        /**
         * 计算权重
         *
         * @param a
         * @param N
         */
        public Map<String,Double> countWeightOneMatrix(double[][] a, int N) {
            Map<String,Double> map = new HashMap<>();
            double[] w1 = new double[N];
            double[] weight = new double[N];
            double sum = 0.0;

            //按行求和
            for (int j = 0; j < N; j++) {
                double t = 0.0;
                for (int l = 0; l < N; l++)
                    t += a[l][j];
                w1[j] = t;
            }

            //按行归一化，然后按列求和，最后得到特征向量w2
            for (int k = 0; k < N; k++) {
                sum = 0;
                for (int i = 0; i < N; i++) {
                    sum = sum + a[k][i] / w1[i];
                }
                weight[k] = sum / N;
            }

            lamta = 0.0;

            //求矩阵和特征向量的积,然后求出特征值
            for (int k = 0; k < N; k++) {
                sum = 0;
                for (int i = 0; i < N; i++) {
                    sum = sum + a[k][i] * weight[i];
                }
                w1[k] = sum;
                lamta = lamta + w1[k] / weight[k];
            }

            // 计算矩阵最大特征值lamta，CI，RI
            lamta = lamta / N;

            double CI = (lamta - N) / (N - 1);

            if (RI[N - 1] != 0) {
                CR = CI / RI[N - 1];
            }

            // 四舍五入处理
            lamta = round(lamta, 3);
            //CI = round(CI, 3);
            CR = round(CR, 3);

            for (int i = 0; i < N; i++) {
                w1[i] = round(w1[i], 4);
                weight[i] = round(weight[i], 4);
            }
            map.put("CR", CR);
            for (int i = 0; i < N; i++) {
                map.put(String.valueOf(i), weight[i]);
            }
            return map;
        }

        /**
         * 四舍五入
         *
         * @param v
         * @param scale
         * @return
         */
        public double round(double v, int scale) {
            if (scale < 0) {
                throw new IllegalArgumentException(
                        "The scale must be a positive integer or zero");
            }
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        /**
         * 返回随机一致性比率
         *
         * @return
         */
        public double getCR() {
            return CR;
        }
    }

