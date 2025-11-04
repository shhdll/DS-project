
public class Reviews{
    private LinkedList<Review> reviewList;

    public void addReview(int reviewId, int productId, int rating, int customerId, String comment) throws InvalidRatingException{
            Review r = new Review(/*reviewId, productId, */rating, /*customerId,*/ comment);
            reviewList.insert(r);
        }
    public void editReview(Review r, int rating,String comment)throws InvalidRatingException{
        r.edit(rating, comment);

    }
    //get Avrage
    
 

}

