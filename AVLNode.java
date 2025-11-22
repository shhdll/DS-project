enum Balance {
    Left, Zero, Right
}
//we can move enum to a separate file 
public class AVLNode<T> {
    public int key;
    public T data;
    public Balance bal;
    public AVLNode<T> left, right;

    public AVLNode(int key, T data) {
        this.key = key;
        this.data = data;
        bal = Balance.Zero;
        left = right = null;
    }
}

