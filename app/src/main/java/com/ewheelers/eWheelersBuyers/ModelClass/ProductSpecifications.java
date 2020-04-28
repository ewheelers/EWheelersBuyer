package com.ewheelers.eWheelersBuyers.ModelClass;

import java.io.Serializable;

public class ProductSpecifications implements Serializable {
    private String prodspecid;
    private String productspecname;
    private String productspecvalue;

    public ProductSpecifications(String prodspecid, String productspecname, String productspecvalue) {
        this.prodspecid = prodspecid;
        this.productspecname = productspecname;
        this.productspecvalue = productspecvalue;
    }

    public ProductSpecifications() {
    }

    public String getProdspecid() {
        return prodspecid;
    }

    public void setProdspecid(String prodspecid) {
        this.prodspecid = prodspecid;
    }

    public String getProductspecname() {
        return productspecname;
    }

    public void setProductspecname(String productspecname) {
        this.productspecname = productspecname;
    }

    public String getProductspecvalue() {
        return productspecvalue;
    }

    public void setProductspecvalue(String productspecvalue) {
        this.productspecvalue = productspecvalue;
    }
}
