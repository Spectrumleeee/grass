/**
 * Author:  C_G <cg.fork@gmail.com>
 * Created: 2015-10-21
 */
package org.cgfork.grass.common.statistic;

/**
 * 
 */
public class Histogram {
    private static long[] BucketLimit;
    
    static {
        BucketLimit = new long[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 25, 30, 35, 40, 45,
            50, 60, 70, 80, 90, 100, 120, 140, 160, 180, 200, 250, 300, 350, 400, 450,
            500, 600, 700, 800, 900, 1000, 1200, 1400, 1600, 1800, 2000, 2500, 3000,
            3500, 4000, 4500, 5000, 6000, 7000, 8000, 9000, 10000, 12000, 14000,
            16000, 18000, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 60000,
            70000, 80000, 90000, 100000, 120000, 140000, 160000, 180000, 200000,
            250000, 300000, 350000, 400000, 450000, 500000, 600000, 700000, 800000,
            900000, 1000000, 1200000, 1400000, 1600000, 1800000, 2000000, 2500000,
            3000000, 3500000, 4000000, 4500000, 5000000, 6000000, 7000000, 8000000,
            9000000, 10000000, 12000000, 14000000, 16000000, 18000000, 20000000,
            25000000, 30000000, 35000000, 40000000, 45000000, 50000000, 60000000,
            70000000, 80000000, 90000000, 100000000, 120000000, 140000000, 160000000,
            180000000, 200000000, 250000000, 300000000, 350000000, 400000000,
            450000000, 500000000, 600000000, 700000000, 800000000, 900000000,
            1000000000, 1200000000, 1400000000, 1600000000, 1800000000, 2000000000,
            2500000000L, 3000000000L, 3500000000L, 4000000000L, 4500000000L,
            5000000000L, 6000000000L, 7000000000L, 8000000000L, 9000000000L,
            Long.MAX_VALUE,
        };
    }
    
    private long maxStatisticValue;
    
    private long minStatisticValue;
    
    private long totalQs;
    
    private long failedQs;
    
    private long sumOfStatisticValues;
    
    private long squarianceOfStatisticValues;
    
    private long []bucketValues;
    
    public Histogram() {
        clear();
    }
    
    public long getTotalQs() {
        return totalQs;
    }

    public long getFailedQs() {
        return failedQs;
    }
    
    public long getMaxStatisticValue() {
        return maxStatisticValue;
    }

    public long getMinStatisticValue() {
        return minStatisticValue;
    }

    public long getSumOfStatisticValues() {
        return sumOfStatisticValues;
    }

    public long getSquarianceOfStatisticValues() {
        return squarianceOfStatisticValues;
    }

    public double getMedianOfStatisticValues() {
        return getStatisticPercentileValue(50.0);
    }
    
    public double getAverageOfStatisticValues() {
        if (totalQs == 0) {
            return 0.0;
        }
        
        return (double) sumOfStatisticValues / (double) totalQs;
    }
    
    public double getStatisticPercentile(long value) {
        if (sumOfStatisticValues == 0) {
            return 100.0;
        }
        for (int sum = 0, i = 0; i < BucketLimit.length; i++) {
            if (BucketLimit[i] > value) {
                return (double) sum * 100 / (double) totalQs;
            }
            sum += bucketValues[i];
        }
        return 100.0;
    }
    
    public double getStatisticPercentileValue(double percent) {
        double threshold = totalQs * percent / 100.0;
        for (int sum = 0, i = 0; i < BucketLimit.length; i++) {
           sum += bucketValues[i];
           
           if (sum >= threshold) {
               long lPoint = i > 0 ? BucketLimit[i - 1] : 0;
               long rPoint = BucketLimit[i];
               double pos = (threshold - (sum - bucketValues[i])) / (double) bucketValues[i];
               double r = lPoint + (rPoint - lPoint) * pos;
               return r > maxStatisticValue ? maxStatisticValue : (r < minStatisticValue ? minStatisticValue: r);
           }
        }
        return maxStatisticValue;
    }
    
    public double getDeviationOfStatisticValues() {
        
        return Math.sqrt(getVarianceOfStatisticValues());
    }
    
    public double getVarianceOfStatisticValues() {
        if (totalQs == 0) {
            return 0.0;
        }
        
        return(squarianceOfStatisticValues - sumOfStatisticValues * sumOfStatisticValues / (double)totalQs)/(double)totalQs;
    }

    public void addValue(long value) {
        totalQs++;
        sumOfStatisticValues += value;
        squarianceOfStatisticValues += value * value;
        
        maxStatisticValue = maxStatisticValue > value ? maxStatisticValue : value;
        minStatisticValue = minStatisticValue < value ? minStatisticValue : value;
        
        for (int i = 0; i < BucketLimit.length; i++) {
            if (BucketLimit[i] > value) {
                bucketValues[i - 1] += 1;
            }
        }
    }
    
    public void addFailedValue(long value) {
        addValue(value);
        failedQs++;
    }

    public void clear() {
        maxStatisticValue = 0;
        minStatisticValue = Long.MAX_VALUE;
        totalQs = 0;
        failedQs = 0;
        sumOfStatisticValues = 0;
        squarianceOfStatisticValues = 0;
        bucketValues = new long[BucketLimit.length];
        for (int i = 0; i < BucketLimit.length; i++) {
            bucketValues[i] = 0;
        }
    }
    
    public Histogram merge(final Histogram h) {
        maxStatisticValue = maxStatisticValue > h.maxStatisticValue ? maxStatisticValue : h.maxStatisticValue;
        minStatisticValue = minStatisticValue < h.minStatisticValue ? minStatisticValue : h.minStatisticValue;
        totalQs += h.totalQs;
        sumOfStatisticValues += h.sumOfStatisticValues;
        squarianceOfStatisticValues += h.squarianceOfStatisticValues;
        for (int i = 0; i < BucketLimit.length; i++) {
            bucketValues[i] += h.bucketValues[i];
        }
        return this;
    }
    
    public Histogram merge(final Histogram ...hs) {
        for (Histogram h : hs) {
            merge(h);
        }
        return this;
    }

    public static Histogram mergeAll(final Histogram ...hs) {
        Histogram mergedContext = new Histogram();
        return mergedContext.merge(hs);
    }
}
