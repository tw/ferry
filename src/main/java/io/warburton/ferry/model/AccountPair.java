package io.warburton.ferry.model;

/**
 * @author tw
 */
public class AccountPair {
    private Account left;
    private Account right;

    public AccountPair(Account left, Account right) {
        if (left.hashCode() > right.hashCode()) {
            this.left = left;
            this.right = right;
        } else {
            this.right = left;
            this.left = right;
        }
    }

    public Account getLeft() {
        return left;
    }

    public Account getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountPair that = (AccountPair) o;

        if (left.equals(that.left) && right.equals(that.right)) {
            return true;
        } else if (left.equals(that.right) && right.equals(that.left)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

}
