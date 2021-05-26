package com.corpogas.corpoapp.Entities.Sucursales;

import com.corpogas.corpoapp.Entities.Catalogos.Product;

import java.io.Serializable;

public class ProductControl implements Serializable {

    public long BranchId;
    public Branch Branch;
    public long ProductId;
    public Product Product;
    public double Price;
    public double Cost;
//    public DateTime StartTime;
//    public DateTime EndTime;


    public long getBranchId() { return BranchId; }

    public void setBranchId(long branchId) { BranchId = branchId; }

    public Branch getBranch() { return Branch; }

    public void setBranch(Branch branch) { Branch = branch; }

    public long getProductId() { return ProductId; }

    public void setProductId(long productId) { ProductId = productId; }

    public Product getProduct () { return Product; }

    public void setProduct(Product product) { Product = product; }

    public double getPrice() { return Price; }

    public void setPrice(double price) { Price = price; }

    public double getCost() { return Cost; }

    public void setCost(double cost) { Cost = cost; }
}
