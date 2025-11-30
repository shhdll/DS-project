public class AVLNode<T> {
    public int key;  
    public T data;
    public int height;
    public Balance bal;
    public AVLNode<T> left, right;

    public AVLNode(int key, T data) {
        this.key = key;
        this.data = data;
        this.height = 1;
        bal = Balance.Zero;
        left = right = null;
    }
}

