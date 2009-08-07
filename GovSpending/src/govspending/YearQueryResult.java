package govspending;

/**
 * Results of a query for the given search term
 * @author Jarret
 */
public class YearQueryResult {
    private ContractsList matches;
    private String queryTerm;
    private int year;
    public YearQueryResult(int year, String queryTerm, ContractsList matches){
        this.year = year;
        this.queryTerm = queryTerm;
        this.matches = matches;
    }

    public int getYear(){
        return year;
    }

    public String getQueryTerm(){
        return queryTerm;
    }

    public ContractsList getMatches(){
        return matches;
    }

    public String toString(){
        return Integer.toString(year) + " " + queryTerm + ": \n" + matches;
    }
}
