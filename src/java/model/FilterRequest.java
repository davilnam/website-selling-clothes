/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;

/**
 *
 * @author PC
 */
public class FilterRequest implements Serializable{
    private String fromDate;
    private String toDate;
    private String filterCriteria;

    public FilterRequest() {
    }

    public FilterRequest(String fromDate, String toDate, String filterCriteria) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.filterCriteria = filterCriteria;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(String filterCriteria) {
        this.filterCriteria = filterCriteria;
    }
    
     
    
}
