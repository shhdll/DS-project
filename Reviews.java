package ecommerce_project;

import java.io.File;
import java.util.Scanner;

public class Reviews{
 Private int reviewId;
 Private int productId;
 Private int rating;
 Private int customerId;
 private String comment;

    public Reviews(int reviewId, int productId, int rating, int customerId, String comment) {
            this.reviewId = reviewId;
            this.productId = productId;
            this.rating = rating;
            this.customerId = customerId;
            this.comment = comment;
        }
 public int getReviewId() {
        return reviewId; 
        }

 public int getProductId() {
        return productId;
    }
 public int getRating() {
        return rating;
    }
 public int getCustomerId() {
        return customerId;
    }
 public String getComment() {
        return comment;
    }
 public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    public void setProductId(int productId) {
            this.productId = productId;
        }
public void displayReviews() {
        System.out.println("Review ID: " + reviewId);
        System.out.println("Product ID: " + productId);
        System.out.println("Rating: " + rating);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Comment: " + comment);
    }


}

