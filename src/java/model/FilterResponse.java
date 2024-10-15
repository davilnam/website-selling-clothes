/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author PC
 */
public class FilterResponse implements Serializable{
    private List<String> labels;
    private List<Double> amounts;

    public FilterResponse() {
    }
    
    public FilterResponse(List<String> labels, List<Double> amounts) {
        this.labels = labels;
        this.amounts = amounts;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Double> amounts) {
        this.amounts = amounts;
    }
    
}
