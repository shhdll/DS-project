public class AVLNode<T> {
    T data;
    AVLNode<T> left, right;
    int height;

    public AVLNode(T data) {
        this.data = data;
        this.height = 1;
    }
}
