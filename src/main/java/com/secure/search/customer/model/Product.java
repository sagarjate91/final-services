package com.secure.search.customer.model;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private int productId;
    @ColumnTransformer(read = "AES_DECRYPT(file_name,'mySecretKey')",write ="AES_ENCRYPT(?,'mySecretKey')")
    @Column(name ="file_name")
    private String fileName;
    @ColumnTransformer(read = "AES_DECRYPT(post_name,'mySecretKey')",write ="AES_ENCRYPT(?,'mySecretKey')")
    @Column(name = "post_name")
    private String postName;
    @ColumnTransformer(read = "AES_DECRYPT(category,'mySecretKey')",write ="AES_ENCRYPT(?,'mySecretKey')")
    private String category;
    @ColumnTransformer(read = "AES_DECRYPT(model_no,'mySecretKey')",write ="AES_ENCRYPT(?,'mySecretKey')")
    @Column(name = "model_no")
    private String modelNo;
    private Long price;
    private Integer quantity;
    @ColumnTransformer(read = "AES_DECRYPT(description,'mySecretKey')",write ="AES_ENCRYPT(?,'mySecretKey')")
    private String description;
    private int active;
    private int view;

    @Transient
    private MultipartFile file;

    public Product(){

    }

    public Product(int productId, String fileName, String postName, String category, String modelNo, Long price, Integer quantity, String description, int active, int view, MultipartFile file) {
        this.productId = productId;
        this.fileName = fileName;
        this.postName = postName;
        this.category = category;
        this.modelNo = modelNo;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.active = active;
        this.view = view;
        this.file = file;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
