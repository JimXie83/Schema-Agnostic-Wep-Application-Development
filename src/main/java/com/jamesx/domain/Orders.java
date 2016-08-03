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
@Table(name = "orders", catalog = "agnostic"  )
@EntityRestUrl(url = "/api/orders/")    // URL for entity Orders, e.g. /api/emp/
@NamedEntityGraph(name = "graph.Orders.eagerLoad", attributeNodes = { @NamedAttributeNode("orderdetails") })
@SmartTableInfo(Captions = "Id,Order No.,Order Date,Shipping Date,Status,Courier,Handler,Description,Valid",Fields = "id,orderNo,orderDate,shippingDate,orderStatus,courier,orderHandler,description,valid")
public class Orders implements java.io.Serializable, IObjectWithState {
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

    private java.lang.String orderNo;
    
    @Column(name = "order_no", nullable = false, length = 10 )
    public java.lang.String getOrderNo() {
        return this.orderNo;
    }
    public void setOrderNo(java.lang.String orderNo) {
        this.orderNo = orderNo;
    }

    private java.util.Date orderDate;
    
    @Column(name = "order_date", nullable = false, length = 10 )
    public java.util.Date getOrderDate() {
        return this.orderDate;
    }
    public void setOrderDate(java.util.Date orderDate) {
        this.orderDate = orderDate;
    }

    private java.util.Date shippingDate;
    
    @Column(name = "shipping_date", nullable = true, length = 10 )
    public java.util.Date getShippingDate() {
        return this.shippingDate;
    }
    public void setShippingDate(java.util.Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    private java.lang.String orderStatus;
    
    @Column(name = "order_status", nullable = true, length = 16 )
    public java.lang.String getOrderStatus() {
        return this.orderStatus;
    }
    public void setOrderStatus(java.lang.String orderStatus) {
        this.orderStatus = orderStatus;
    }

    private java.lang.String courier;
    
    @Column(name = "courier", nullable = true, length = 16 )
    public java.lang.String getCourier() {
        return this.courier;
    }
    public void setCourier(java.lang.String courier) {
        this.courier = courier;
    }

    private java.lang.String orderHandler;
    
    @Column(name = "order_handler", nullable = true, length = 40 )
    public java.lang.String getOrderHandler() {
        return this.orderHandler;
    }
    public void setOrderHandler(java.lang.String orderHandler) {
        this.orderHandler = orderHandler;
    }

    private java.lang.String description;
    
    @Column(name = "description", nullable = true, length = 40 )
    public java.lang.String getDescription() {
        return this.description;
    }
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    private java.lang.Boolean valid;
    
    @Column(name = "valid", nullable = true, length = 1 )
    public java.lang.Boolean getValid() {
        return this.valid;
    }
    public void setValid(java.lang.Boolean valid) {
        this.valid = valid;
    }
    
    //oneToMany relationship with child table
    private Set<Orderdetails> orderdetails = new HashSet<Orderdetails>(0);  
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orders", cascade = CascadeType.MERGE,orphanRemoval=true) @JsonManagedReference
    public Set<Orderdetails> getOrderdetails() {return this.orderdetails;}
    public void setOrderdetails(Set<Orderdetails> orderdetails) {this.orderdetails = orderdetails;}
   
 
    /* default constructor */
    public Orders() { }
    
    @Override @Transient
    public ObjectState getObjectState() {
        return this.objectState;
    }
    @Override
    public void setObjectState(ObjectState objectState) {
        this.objectState = objectState;
    }
}