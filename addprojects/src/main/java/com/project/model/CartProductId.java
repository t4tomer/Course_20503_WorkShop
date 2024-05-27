package com.project.model;

// $ this class is used so that the cartProduct will have composite primary key
//$ meeaning that there are 2 primary keys emailCustomer and productCodeInCart
import java.io.Serializable;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
public class CartProductId implements Serializable {

    private String emailCustomer;
    private String productCodeInCart;

    public CartProductId() {
    }

    public CartProductId(String emailCustomer, String productCodeInCart) {
        this.emailCustomer = emailCustomer;
        this.productCodeInCart = productCodeInCart;
    }

    // Getters and setters

    public String getEmailCustomer() {
        return emailCustomer;
    }

    public void setEmailCustomer(String emailCustomer) {
        this.emailCustomer = emailCustomer;
    }

    public String getproductCodeInCart() {
        return productCodeInCart;
    }

    public void setproductCodeInCart(String productCodeInCart) {
        this.productCodeInCart = productCodeInCart;
    }

    // Override equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CartProductId that = (CartProductId) o;

        if (!emailCustomer.equals(that.emailCustomer))
            return false;
        return productCodeInCart.equals(that.productCodeInCart);
    }

    @Override
    public int hashCode() {
        int result = emailCustomer.hashCode();
        result = 31 * result + productCodeInCart.hashCode();
        return result;
    }
}
