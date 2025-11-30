public class AVLTree<T> {

AVLNode<T> root, current;

    public AVLTree() {  
        root = current = null;  //starts an empty tree
    }

    public boolean empty() {
        return root == null;
    }

     // Retrieve the data from the current node
    public T retrieve() {
        return (current != null) ? current.data : null;
    }

    // Search for a key in the tree, update current to found node 
    // O(log n)
    public boolean findkey(int tkey) {
        AVLNode<T> p = root;
        while (p != null) {
            current = p;
            if (p.key == tkey) {
                return true;  // key found
            } else if (tkey < p.key) {
                p = p.left;  // go left
            } else {
                p = p.right;  // go right
            }
        }
        return false;  // key not found
    }

    // Insert 
    // O(log n)
    public boolean insert(int key, T val) {
        if (findkey(key)) {
            return false; // prevent duplicate key
        }
        root = insertRec(root, key, val);  // recursively insert and rebalance
        return true;
    }

    private AVLNode<T> insertRec(AVLNode<T> node, int key, T val) {
        if (node == null) {
            AVLNode<T> newNode = new AVLNode<>(key, val);
            current = newNode; // Update cursor
            return newNode;
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key, val);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, val);
        } else {
            return node; 
        }

        // Rebalance this specific node 
        // O(1)
        return rebalance(node);
    }

    // Remove a key from the tree
    // O(log n)
    public boolean removeKey(int key) {
        if (!findkey(key)) return false; // Not found
        root = deleteRec(root, key);  // recursively delete and rebalance
        current = root; // Reset cursor to root 
        return true;
    }

    private AVLNode<T> deleteRec(AVLNode<T> node, int key) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            // Node found
            if ((node.left == null) || (node.right == null)) {
                // No child or One child
                node = (node.left != null) ? node.left : node.right;
            } else {
                // Two children, Get successor (smallest in right subtree)
                AVLNode<T> temp = getMin(node.right);
                node.key = temp.key;
                node.data = temp.data;
                node.right = deleteRec(node.right, temp.key);
            }
        }

        if (node == null) return null;

        // Rebalance this specific node
        return rebalance(node);
    }
    
    // Subtree deletion
    public void deleteSub() {
        if (current == null) return;
        root = deleteSubRec(root, current.key);
        current = root;
    }

    private AVLNode<T> deleteSubRec(AVLNode<T> node, int key) {
        if (node == null) return null;
        
        if (key < node.key) {
            node.left = deleteSubRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteSubRec(node.right, key);
        } else {
            return null; 
        }
        return rebalance(node);  // Rebalance the path back
    }

    // helpers for rebalancing
    
    private AVLNode<T> rebalance(AVLNode<T> node) {
        updateHeight(node);
        int balance = getBalance(node);

        // Left Heavy
        if (balance > 1) {
            if (getBalance(node.left) >= 0) {
                return rotateRight(node); // LL Case
            } else {
                node.left = rotateLeft(node.left); // LR Case
                return rotateRight(node);
            }
        }

        // Right Heavy
        if (balance < -1) {
            if (getBalance(node.right) <= 0) {
                return rotateLeft(node); // RR Case
            } else {
                node.right = rotateRight(node.right); // RL Case
                return rotateLeft(node);
            }
        }

        return node;
    }

    private int height(AVLNode<T> node) {
        return (node == null) ? 0 : node.height;
    }
    
    private void updateHeight(AVLNode<T> node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    private int getBalance(AVLNode<T> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode<T> getMin(AVLNode<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Rotations with height updates

    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights 
        updateHeight(y);
        updateHeight(x);

        return x; // New root
    }

    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y; // New root
    }

    // Traversals methods
    public void traverse(TraversalOrder ord) {
        traverseSub(root, ord);
    }

    private void traverseSub(AVLNode<T> node, TraversalOrder ord) {
        if (node == null) return;
        switch (ord) {
            case PreOrder:
                visit(node);
                traverseSub(node.left, ord);
                traverseSub(node.right, ord);
                break;
            case InOrder:
                traverseSub(node.left, ord);
                visit(node);
                traverseSub(node.right, ord);
                break;
            case PostOrder:
                traverseSub(node.left, ord);
                traverseSub(node.right, ord);
                visit(node);
                break;
        }
    }

    private void visit(AVLNode<T> node) {
        current = node;  // mark current node during traversal
    }
    
    public AVLNode<T> getRoot() {
        return root;
    }
}