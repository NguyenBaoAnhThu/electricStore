package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productImages")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productImageID;
    private String imageURL;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getProductImageID() {
        return productImageID;
    }

    public void setProductImageID(Integer productImageID) {
        this.productImageID = productImageID;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


}
