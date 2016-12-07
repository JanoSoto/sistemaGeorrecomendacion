/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author JAno
 */
public class Correlation {
    private long user1_id;
    private long user2_id;
    private double correlation_value;

    public Correlation(long user1_id, long user2_id, double correlation_value) {
        this.user1_id = user1_id;
        this.user2_id = user2_id;
        this.correlation_value = correlation_value;
    }

    public long getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(long user1_id) {
        this.user1_id = user1_id;
    }

    public long getUser2_id() {
        return user2_id;
    }

    public void setUser2_id(long user2_id) {
        this.user2_id = user2_id;
    }

    public double getCorrelation_value() {
        return correlation_value;
    }

    public void setCorrelation_value(double correlation_value) {
        this.correlation_value = correlation_value;
    }
        
}
