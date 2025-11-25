public class Reviews {

    private AVLTree<Review> reviewList = new AVLTree<>();


    public Reviews() {
        this.reviewList = new AVLTree<>();
    }

    public void addReview(int reviewId, int productId, int rating, int customerId, String comment)
            throws InvalidRatingException {
        Review r = new Review(reviewId, productId, rating, customerId, comment);
        reviewList.insert(reviewId,r);
    }

    public void editReview(Review r, int rating, String comment) throws InvalidRatingException {
        r.edit(rating, comment);
    }

    public Review findReviewById(int reviewId) {
        return findReviewRecursive(reviewList.getRoot(), reviewId);
    }

    private Review findReviewRecursive(AVLNode<Review> node, int reviewId) {
        if (node == null) return null;

        if (reviewId == node.key) {
            return node.data;
        } else if (reviewId < node.key) {
            return findReviewRecursive(node.left, reviewId);
        } else {
            return findReviewRecursive(node.right, reviewId);
        }
    }

    public double AvrageRating() {
        if (reviewList.empty()) return 0.0;

        double[] result = computeAvg(reviewList.getRoot()); 
        double sum = result[0];
        double count = result[1];

        if (count == 0) return 0.0;
        return sum / count;
    }

    // returns sum and count
    private double[] computeAvg(AVLNode<Review> node) {
        if (node == null) return new double[]{0, 0};

        double[] left = computeAvg(node.left);
        double[] right = computeAvg(node.right);

        double sum = left[0] + right[0] + node.data.getRating();
        double count = left[1] + right[1] + 1;

        return new double[]{sum, count};
    }


    public void addReview(Review r) {
        reviewList.insert(r.getReviewId(),r);
    }
}
