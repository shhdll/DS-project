public class Review {
    private int reviewId;
 private int productId;
 private int rating;
 private int customerId ;
 private String comment;

    public Review(int reviewId, int productId, int rating, int customerId, String comment) {
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
}
