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
@Table(name = "category", catalog = "agnostic"  )
@EntityRestUrl(url = "/api/category/")    // URL for entity Category, e.g. /api/emp/
@NamedEntityGraph(name = "graph.Category.eagerLoad", attributeNodes = {})
@SmartTableInfo(Captions = "Id,Category No.,Category Name,Note,Date Created,Active",Fields = "id,categoryNo,categoryName,note,dateCreated,isActive")
public class Category implements java.io.Serializable, IObjectWithState {
    @Transient
    ObjectState objectState=ObjectState.Unchanged;
    

    private java.lang.Integer id;
    @Id @GeneratedValue 
    @Column(name = "id", nullable = false, length = 11 )
    public java.lang.Integer getId() {
        return this.id;
    }
    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    private java.lang.String categoryNo;
    
    @Column(name = "CategoryNo", nullable = false, length = 40 )
    public java.lang.String getCategoryNo() {
        return this.categoryNo;
    }
    public void setCategoryNo(java.lang.String categoryNo) {
        this.categoryNo = categoryNo;
    }

    private java.lang.String categoryName;
    
    @Column(name = "CategoryName", nullable = false, length = 40 )
    public java.lang.String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(java.lang.String categoryName) {
        this.categoryName = categoryName;
    }

    private java.lang.String note;
    
    @Column(name = "Note", nullable = true, length = 50 )
    public java.lang.String getNote() {
        return this.note;
    }
    public void setNote(java.lang.String note) {
        this.note = note;
    }

    private java.util.Date dateCreated;
    
    @Column(name = "DateCreated", nullable = true, length = 10 )
    public java.util.Date getDateCreated() {
        return this.dateCreated;
    }
    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    private java.lang.Boolean isActive;
    
    @Column(name = "IsActive", nullable = true, length = 1 )
    public java.lang.Boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(java.lang.Boolean isActive) {
        this.isActive = isActive;
    }

 
    /* default constructor */
    public Category() { }
    
    @Override @Transient
    public ObjectState getObjectState() {
        return this.objectState;
    }
    @Override
    public void setObjectState(ObjectState objectState) {
        this.objectState = objectState;
    }
}