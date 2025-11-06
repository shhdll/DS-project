
public class Reviews {
    private LinkedList<Review> reviewList;

    public Reviews() {
        this.reviewList = new LinkedList<Review>();
    }

    public void addReview(int reviewId, int productId, int rating, int customerId, String comment)
            throws InvalidRatingException {
        Review r = new Review(reviewId, productId, rating, customerId, comment);
        reviewList.insert(r);
    }

    public void editReview(Review r, int rating, String comment) throws InvalidRatingException {
        r.edit(rating, comment);

    }

    public double AvrageRating() {
        if (reviewList == null || reviewList.empty()) {
            return 0;
        }
        double sum = 0;
        int count = 0;
        Node<Review> tmp = reviewList.getHead();
        while (tmp != null) {
            sum += tmp.data.getRating();
            count++;
            tmp = tmp.next;
        }
        if (count == 0)
            return 0.0;

        return sum / count;
    }

}
