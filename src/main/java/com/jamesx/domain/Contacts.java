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
@Table(name = "contacts", catalog = "agnostic"  )
@EntityRestUrl(url = "/api/contacts/")    // URL for entity Contacts, e.g. /api/emp/
@NamedEntityGraph(name = "graph.Contacts.eagerLoad", attributeNodes = {})
@SmartTableInfo(Captions = "Id,Last Name,First Name,Birthdate,Place of Birth,Country,State,City,Street,Zip",Fields = "id,lastName,firstName,birthday,brithplace,country,state,city,street,zip")
public class Contacts implements java.io.Serializable, IObjectWithState {
    @Transient
    ObjectState objectState=ObjectState.Unchanged;
    

    private java.lang.Integer id;
    @Id @GeneratedValue 
    @Column(name = "contact_id", nullable = false, length = 11 )
    public java.lang.Integer getId() {
        return this.id;
    }
    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    private java.lang.String lastName;
    
    @Column(name = "last_name", nullable = false, length = 30 )
    public java.lang.String getLastName() {
        return this.lastName;
    }
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    private java.lang.String firstName;
    
    @Column(name = "first_name", nullable = true, length = 25 )
    public java.lang.String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    private java.util.Date birthday;
    
    @Column(name = "birthday", nullable = true, length = 10 )
    public java.util.Date getBirthday() {
        return this.birthday;
    }
    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    private java.lang.String brithplace;
    
    @Column(name = "brithplace", nullable = true, length = 60 )
    public java.lang.String getBrithplace() {
        return this.brithplace;
    }
    public void setBrithplace(java.lang.String brithplace) {
        this.brithplace = brithplace;
    }

    private java.lang.String country;
    
    @Column(name = "country", nullable = true, length = 40 )
    public java.lang.String getCountry() {
        return this.country;
    }
    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    private java.lang.String state;
    
    @Column(name = "state", nullable = true, length = 40 )
    public java.lang.String getState() {
        return this.state;
    }
    public void setState(java.lang.String state) {
        this.state = state;
    }

    private java.lang.String city;
    
    @Column(name = "city", nullable = true, length = 40 )
    public java.lang.String getCity() {
        return this.city;
    }
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    private java.lang.String street;
    
    @Column(name = "street", nullable = true, length = 40 )
    public java.lang.String getStreet() {
        return this.street;
    }
    public void setStreet(java.lang.String street) {
        this.street = street;
    }

    private java.lang.String zip;
    
    @Column(name = "zip", nullable = true, length = 40 )
    public java.lang.String getZip() {
        return this.zip;
    }
    public void setZip(java.lang.String zip) {
        this.zip = zip;
    }

 
    /* default constructor */
    public Contacts() { }
    
    @Override @Transient
    public ObjectState getObjectState() {
        return this.objectState;
    }
    @Override
    public void setObjectState(ObjectState objectState) {
        this.objectState = objectState;
    }
}