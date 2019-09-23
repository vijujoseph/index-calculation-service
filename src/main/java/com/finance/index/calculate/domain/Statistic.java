package com.finance.index.calculate.domain;

public class Statistic {

    private double avg;
    private double max;
    private double min;
    private double sum;
    private long count;

    public Statistic() {
    }

    public Statistic(double avg, double max, double min, double sum, long count) {
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.sum = sum;
        this.count = count;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistic statistic = (Statistic) o;

        if (Double.compare(statistic.avg, avg) != 0) return false;
        if (Double.compare(statistic.max, max) != 0) return false;
        if (Double.compare(statistic.min, min) != 0) return false;
        if (Double.compare(statistic.sum, sum) != 0) return false;
        return count == statistic.count;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(avg);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(min);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(sum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", sum=" + sum +
                ", count=" + count +
                '}';
    }
}
