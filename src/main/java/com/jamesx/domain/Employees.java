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
@Table(name = "employees", catalog = "agnostic"  )
@EntityRestUrl(url = "/api/employees/")    // URL for entity Employees, e.g. /api/emp/
@NamedEntityGraph(name = "graph.Employees.eagerLoad", attributeNodes = { @NamedAttributeNode("salaries") , @NamedAttributeNode("titles") })
@SmartTableInfo(Captions = "Id,Employee No.,Date of Birth,First Name,Last Name,Gender,Hire Date,Active",Fields = "id,empNo,birthDate,firstName,lastName,gender,hireDate,active")
public class Employees implements java.io.Serializable, IObjectWithState {
    @Transient
    ObjectState objectState=ObjectState.Unchanged;
    

    private java.lang.Integer id;
    @Id @GeneratedValue 
    @Column(name = "emp_id", nullable = false, length = 11 )
    public java.lang.Integer getId() {
        return this.id;
    }
    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    private java.lang.String empNo;
    
    @Column(name = "emp_no", nullable = false, length = 6 )
    public java.lang.String getEmpNo() {
        return this.empNo;
    }
    public void setEmpNo(java.lang.String empNo) {
        this.empNo = empNo;
    }

    private java.util.Date birthDate;
    
    @Column(name = "birth_date", nullable = false, length = 10 )
    public java.util.Date getBirthDate() {
        return this.birthDate;
    }
    public void setBirthDate(java.util.Date birthDate) {
        this.birthDate = birthDate;
    }

    private java.lang.String firstName;
    
    @Column(name = "first_name", nullable = true, length = 14 )
    public java.lang.String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    private java.lang.String lastName;
    
    @Column(name = "last_name", nullable = true, length = 16 )
    public java.lang.String getLastName() {
        return this.lastName;
    }
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    private java.lang.String gender;
    
    @Column(name = "gender", nullable = true, length = 6 )
    public java.lang.String getGender() {
        return this.gender;
    }
    public void setGender(java.lang.String gender) {
        this.gender = gender;
    }

    private java.util.Date hireDate;
    
    @Column(name = "hire_date", nullable = false, length = 10 )
    public java.util.Date getHireDate() {
        return this.hireDate;
    }
    public void setHireDate(java.util.Date hireDate) {
        this.hireDate = hireDate;
    }

    private java.lang.Boolean active;
    
    @Column(name = "active", nullable = true, length = 1 )
    public java.lang.Boolean getActive() {
        return this.active;
    }
    public void setActive(java.lang.Boolean active) {
        this.active = active;
    }
    
    //oneToMany relationship with child table
    private Set<Salaries> salaries = new HashSet<Salaries>(0);  
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employees", cascade = CascadeType.MERGE,orphanRemoval=true) @JsonManagedReference
    public Set<Salaries> getSalaries() {return this.salaries;}
    public void setSalaries(Set<Salaries> salaries) {this.salaries = salaries;}
       
    //oneToMany relationship with child table
    private Set<Titles> titles = new HashSet<Titles>(0);  
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employees", cascade = CascadeType.MERGE,orphanRemoval=true) @JsonManagedReference
    public Set<Titles> getTitles() {return this.titles;}
    public void setTitles(Set<Titles> titles) {this.titles = titles;}
   
 
    /* default constructor */
    public Employees() { }
    
    @Override @Transient
    public ObjectState getObjectState() {
        return this.objectState;
    }
    @Override
    public void setObjectState(ObjectState objectState) {
        this.objectState = objectState;
    }
}