public class Review {
 private int reviewId;
 private int productId;
 private int rating;
 private int customerId ;
 private String comment;

    public Review(int reviewId, int productId, int rating,  int customerId, String comment)  throws InvalidRatingException{
            this.reviewId = reviewId;
            this.productId = productId;
            setRating(rating);
            this.customerId = customerId;
            this.comment = comment;
        }
    public void edit(int rating,String comment) throws InvalidRatingException{
        setRating(rating);
        this.comment=comment;
    }
    public void setRating(int rating) throws InvalidRatingException {
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException("Invalid rating: " + rating + ". Rating must be between 1 and 5.");
        }
        this.rating = rating;
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
        System.out.println("Rating: " + rating + " ★");
        System.out.println("Comment: " + comment);
 }
}
