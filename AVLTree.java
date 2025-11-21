public class AVLTree<T extends Comparable<T>> {

    private AVLNode<T> root;

    public AVLNode<T> getRoot() {
        return root;
    }

    // height helper
    private int height(AVLNode<T> node) {
        return node == null ? 0 : node.height;
    }

    // balance factor
    private int getBalance(AVLNode<T> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // right rotation
    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> temp = x.right;

        x.right = y;
        y.left = temp;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // left rotation
    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> temp = y.left;

        y.left = x;
        x.right = temp;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // insert public method
    public void insert(T value) {
        root = insertRec(root, value);
    }

    // insert recursive
    private AVLNode<T> insertRec(AVLNode<T> node, T value) {

        if (node == null)
            return new AVLNode<>(value);

        int cmp = value.compareTo(node.data);

        if (cmp < 0)
            node.left = insertRec(node.left, value);
        else if (cmp > 0)
            node.right = insertRec(node.right, value);
        else
            return node; // duplicates ignored

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 && value.compareTo(node.left.data) < 0)
            return rotateRight(node);

        // Right Right
        if (balance < -1 && value.compareTo(node.right.data) > 0)
            return rotateLeft(node);

        // Left Right
        if (balance > 1 && value.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left
        if (balance < -1 && value.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // search
    public T search(T key) {
        AVLNode<T> curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.data);
            if (cmp == 0) return curr.data;
            curr = (cmp < 0) ? curr.left : curr.right;
        }
        return null;
    }

    // inorder traversal
    public void printInOrder() {
        printInOrderRec(root);
    }

    private void printInOrderRec(AVLNode<T> node) {
        if (node == null) return;
        printInOrderRec(node.left);
        System.out.println(node.data);
        printInOrderRec(node.right);
    }
}
