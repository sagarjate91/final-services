package com.secure.search.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_RECOVER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRecover {

    @Id
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


}
