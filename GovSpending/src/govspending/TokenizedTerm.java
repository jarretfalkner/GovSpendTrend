package govspending;

/**
 * Holds a raw and stemmed term from a query.
 * @author Jarret
 */
public class TokenizedTerm {
    private String stemmedTerm = null;
    private String originalTerm = null;

    public TokenizedTerm(String original, String stemmed){
        this.stemmedTerm = stemmed;
        this.originalTerm = original;
    }

    public int hashCode(){
        return stemmedTerm.hashCode() + originalTerm.hashCode();
    }
    public boolean equals(Object o){
        TokenizedTerm to = (TokenizedTerm) o;
        return to.getOriginalTerm().equals(originalTerm) &&
                to.getStemmedTerm().equals(stemmedTerm);
    }

    /**
     * @return the stemmedTerm
     */
    public String getStemmedTerm() {
        return stemmedTerm;
    }

    /**
     * @return the originalTerm
     */
    public String getOriginalTerm() {
        return originalTerm;
    }

    public String toString(){
        return stemmedTerm + " from " + originalTerm;
    }
}
