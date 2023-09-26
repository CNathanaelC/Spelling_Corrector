package spell;

public class Trie extends Object implements ITrie{
    private Node root = new Node();
    private int wordCount = 0;
    private int nonuniqueWordCount = 0;
    private int nodeCount = 1;

    @Override
    public void add(String word) {
        Node node = root;
        //iterate through word going deeper into Trie until the end of the word
        for(int i = 0; i < word.length(); i++) {
            int index = Character.toLowerCase(word.charAt(i)) - 'a';
            if(node.children[index] == null) {
                node.children[index] = new Node();
                nodeCount++;
            }
            node = node.children[index];
        }
        //Increment the count for the word added
        node.incrementValue();
        //Increment word count if word is unique
        if(node.getValue() == 1) {
            wordCount++;
        }
        nonuniqueWordCount++;
    }

    @Override
    public Node find(String word) {
        Node node = root;
        //iterate through word going deeper into Trie until the end of word
        for(int i = 0; i < word.length(); i++) {
            int index = Character.toLowerCase(word.charAt(i)) - 'a';
            if(node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        //if the word is in the Trie then it will return that node otherwise null
        if(node.getValue() == 0) {
            return null;
        } else {
            return node;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }
    private void toStringHelper(Node node, StringBuilder returnStatement, StringBuilder incomplete) {
        Node[] children = node.getChildren();
        if(node.getValue() > 0) {
            returnStatement.append(incomplete.toString());
            returnStatement.append('\n');
        }
        for(int i = 0; i < 26; i++) {
            if(children[i] != null) {
                char letter = (char)(i + 'a');
                incomplete.append(letter);
                toStringHelper(children[i], returnStatement, incomplete);
                incomplete.deleteCharAt(incomplete.length()-1);
            }
        }
    }
    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     * MUST BE RECURSIVE.
     */
    @Override
    public String toString() {
        StringBuilder returnStatement = new StringBuilder("");
        StringBuilder incomplete = new StringBuilder("");
        Node node = root;
        //iterate through node's children collecting letters until a word is formed or a null node is reached
        toStringHelper(node, returnStatement, incomplete);
        return returnStatement.toString();
    }

    /**
     * Returns the hashcode of this trie.
     * MUST be constant time.
     *
     * @return a uniform, deterministic identifier for this trie.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        Node firstNonNull = root.getChildren()[hash];
        while(firstNonNull == null && hash < 26) {
            firstNonNull = root.getChildren()[hash];
            hash += 1;
        }
        hash = this.getNodeCount() * hash * this.getWordCount();
        return hash;
    }

    /**
     * Checks if an object is equal to this trie.
     * MUST be recursive.
     *
     * @param o Object to be compared against this trie
     * @return true if o is a Trie with same structure and node count for each node
     *         false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Trie object_trie = (Trie)o;
        if (this.wordCount != object_trie.wordCount || this.nodeCount != object_trie.nodeCount) {
            return false;
        }
        return equalshelper(this.root, object_trie.root);
    }
    public boolean equalshelper(INode r, INode o) {
        if(r.getValue() != o.getValue()) {
            return false;
        }
        for (int i = 0; i < 26; i++) {
            if((o.getChildren()[i] == null && r.getChildren()[i] != null) || (r.getChildren()[i] == null && o.getChildren()[i] != null) ) {
                return false;
            }
        }
        for (int i = 0; i < 26; i++) {
            if((o.getChildren()[i] != null && r.getChildren()[i] != null) ) {
                boolean eq = equalshelper(o.getChildren()[i], r.getChildren()[i]);
                if(!eq) {
                    return false;
                }
            }
        }

        return true;
    }
}
