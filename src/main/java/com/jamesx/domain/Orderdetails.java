package com.jamesx.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jamesx.util.IObjectWithState;
import com.jamesx.util.ObjectState;
import com.jamesx.util.myAnnotations.EntityRestUrl;
import com.jamesx.util.myAnnotations.SmartTableInfo;

import javax.persistence.*;

/**************************************************
 * By JamesXie 2016
 **************************************************/
@Entity
@Table(name = "orderdetails", catalog = "agnostic"  )
@EntityRestUrl(url = "/api/orders/")    // URL for entity Orderdetails, e.g. /api/emp/
@NamedEntityGraph(name = "graph.Orderdetails.eagerLoad", attributeNodes = {})
@SmartTableInfo(Captions = "Id,Order Id,Product,Unit Price,Quantity,Discount",Fields = "id,orderId,productName,unitPrice,quantity,discount")
public class Orderdetails implements java.io.Serializable, IObjectWithState {
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
    private Orders orders;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_ID", nullable = false)
    @JsonBackReference
    public Orders getOrders() {return orders;}

    public void setOrders(Orders orders) {this.orders = orders;}

    private String productName;
    
    @Column(name = "product_name", nullable = false, length = 50 )
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    private java.math.BigDecimal unitPrice;
    
    @Column(name = "UnitPrice", nullable = false, length = 14 , precision =12,scale = 2 )
    public java.math.BigDecimal getUnitPrice() {
        return this.unitPrice;
    }
    public void setUnitPrice(java.math.BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    private Integer quantity;
    
    @Column(name = "Quantity", nullable = false, length = 11 )
    public Integer getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    private java.math.BigDecimal discount;
    
    @Column(name = "Discount", nullable = false, length = 4 , precision =2,scale = 2 )
    public java.math.BigDecimal getDiscount() {
        return this.discount;
    }
    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }

 
    /* default constructor */
    public Orderdetails() { }
    
    @Override @Transient
    public ObjectState getObjectState() {
        return this.objectState;
    }
    @Override
    public void setObjectState(ObjectState objectState) {
        this.objectState = objectState;
    }
}