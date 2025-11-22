enum Order {
    PreOrder,
    InOrder,
    PostOrder
}
//we can move enum to a separate file 
public class AVLTree<T> {

    AVLNode<T> root, current;

    public AVLTree() {
        root = current = null;
    }

    public boolean empty() {
        return root == null;
    }

    public boolean full() {
        return false;
    }

    public T retrieve() {
        return current.data;
    }

    public boolean findkey(int tkey) {
        AVLNode<T> p = root, q = root;
        if (empty())
            return false;
        while (p != null) {
            q = p;
            if (p.key == tkey) {
                current = p;
                return true;
            } else if (tkey < p.key)
                p = p.left;
            else
                p = p.right;
        }
        current = q;
        return false;
    }

    public boolean insert(int k, T val) {
        AVLNode<T> p, q = current;
        if (findkey(k)) {
            current = q;
            return false; // key already in the tree
        }
        p = new AVLNode<T>(k, val);
        if (empty()) {
            root = current = p;
            root = rebalanceTree(root);
            return true;
        } else {
            if (k < current.key)
                current.left = p;
            else
                current.right = p;
            current = p;
            root = rebalanceTree(root);
            return true;
        }
    }

    public boolean removeKey(int k) {
        AVLNode<T> p = root;
        AVLNode<T> q = null;
        boolean found = false;

        while ((p != null) && (!found)) {
            int res = k - p.key;
            if (res < 0) {
                q = p;
                p = p.left;
            } else if (res > 0) {
                q = p;
                p = p.right;
            } else
                found = true;
        }

        if (found) {
            if ((p.left != null) && (p.right != null)) {
                AVLNode<T> min = p.right;
                q = p;
                while (min.left != null) {
                    q = min;
                    min = min.left;
                }
                p.key = min.key;
                p.data = min.data;
                deleteNode(min, q);
            } else
                deleteNode(p, q);

            current = root;
            root = rebalanceTree(root);
            return true;
        }
        return false;
    }

    private void deleteNode(AVLNode<T> n, AVLNode<T> parent) {
        AVLNode<T> child;
        if (n.left != null)
            child = n.left;
        else
            child = n.right;
        if (parent == null) {
            root = child;
        } else {
            if (n.key - parent.key < 0)
                parent.left = child;
            else
                parent.right = child;
        }
    }

    public boolean update(int tkey, T val) {
        if (empty())
            return false;
        if (findkey(tkey)) {
            current.data = val;
            return true;
        }
        return false;
    }

    public void traverse(Order ord) {
        traverseSub(root, ord);
    }

    private void traverseSub(AVLNode<T> node, Order ord) {
        if (node == null)
            return;

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
        current = node;
    }

    public void deleteSub() {
        if (current == null || root == null)
            return;
        int key = current.key;
        root = deleteSubRec(root, key);
        current = root;
        root = rebalanceTree(root);
    }

    private AVLNode<T> deleteSubRec(AVLNode<T> node, int key) {
        if (node == null)
            return null;
        if (key < node.key) {
            node.left = deleteSubRec(node.left, key);
            return node;
        } else if (key > node.key) {
            node.right = deleteSubRec(node.right, key);
            return node;
        } else {
            deleteSubtreeNodes(node);
            return null;
        }
    }

    private void deleteSubtreeNodes(AVLNode<T> node) {
        if (node == null)
            return;
        deleteSubtreeNodes(node.left);
        deleteSubtreeNodes(node.right);
        node.left = node.right = null;
    }

    private AVLNode<T> rebalanceTree(AVLNode<T> node) {
        if (node == null)
            return null;
        node.left = rebalanceTree(node.left);
        node.right = rebalanceTree(node.right);
        return rebalance(node);
    }

    private int height(AVLNode<T> node) {
        if (node == null)
            return 0;
        int hl = height(node.left);
        int hr = height(node.right);
        int diff = hr - hl;
        if (diff < 0)
            node.bal = Balance.Left;
        else if (diff > 0)
            node.bal = Balance.Right;
        else
            node.bal = Balance.Zero;
        return 1 + (hl > hr ? hl : hr);
    }

    private int balanceFactor(AVLNode<T> node) {
        if (node == null)
            return 0;
        int hl = height(node.left);
        int hr = height(node.right);
        return hr - hl;
    }

    private AVLNode<T> rebalance(AVLNode<T> node) {
        if (node == null)
            return null;

        int bf = balanceFactor(node);

        if (bf < -1) { 
            if (balanceFactor(node.left) <= 0) {
                node = rotateRight(node); // LL
            } else {
                node.left = rotateLeft(node.left); // LR
                node = rotateRight(node);
            }
        } else if (bf > 1) { 
            if (balanceFactor(node.right) >= 0) {
                node = rotateLeft(node); // RR
            } else {
                node.right = rotateRight(node.right); // RL
                node = rotateLeft(node);
            }
        } else {
            height(node); 
        }
        return node;
    }

    private AVLNode<T> rotateLeft(AVLNode<T> p) {
        AVLNode<T> q = p.right;
        p.right = q.left;
        q.left = p;
        height(p);
        height(q);
        return q;
    }

    private AVLNode<T> rotateRight(AVLNode<T> p) {
        AVLNode<T> q = p.left;
        p.left = q.right;
        q.right = p;
        height(p);
        height(q);
        return q;
    }
   
    public AVLNode<T> getRoot() {
    return root;
}
}
