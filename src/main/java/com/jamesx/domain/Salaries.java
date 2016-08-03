package com.jamesx.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jamesx.util.HelperUtil;
import com.jamesx.util.IObjectWithState;
import com.jamesx.util.ObjectState;
import com.jamesx.util.myAnnotations.EntityRestUrl;
import com.jamesx.util.myAnnotations.SmartTableInfo;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**************************************************
 * By JamesXie 2016
 **************************************************/
@Entity
@Table(name = "salaries", catalog = "agnostic"  )
@EntityRestUrl(url = "/api/employees/")    // URL for entity Salaries, e.g. /api/emp/
@NamedEntityGraph(name = "graph.Salaries.eagerLoad", attributeNodes = {})
@SmartTableInfo(Captions = "Id,Emp Id,Salary,From Date,To Date",Fields = "id,empId,salary,fromDate,toDate")
public class Salaries implements java.io.Serializable, IObjectWithState {
    @Transient
    ObjectState objectState=ObjectState.Unchanged;
    

    private Integer id;
    @Id @GeneratedValue 
    @Column(name = "id", nullable = false, length = 11 )
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    //ManyToOne relationship with parent table
    private Employees employees;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    @JsonBackReference
    public Employees getEmployees() {return employees;}

    public void setEmployees(Employees employees) {this.employees = employees;}

    private Integer salary;
    
    @Column(name = "salary", nullable = true, length = 11 )
    public Integer getSalary() {
        return this.salary;
    }
    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    private java.util.Date fromDate;
    
    @Column(name = "from_date", nullable = true, length = 10 )
    public java.util.Date getFromDate() {
        return this.fromDate;
    }
    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    private java.util.Date toDate;
    
    @Column(name = "to_date", nullable = true, length = 10 )
    public java.util.Date getToDate() {
        return this.toDate;
    }
    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }

 
    /* default constructor */
    public Salaries() { }
    
    @Override @Transient
    public ObjectState getObjectState() {
        return this.objectState;
    }
    @Override
    public void setObjectState(ObjectState objectState) {
        this.objectState = objectState;
    }
}