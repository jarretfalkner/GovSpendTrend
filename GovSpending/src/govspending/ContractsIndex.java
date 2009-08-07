package govspending;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 *
 * @author Jarret
 */
public class ContractsIndex {
    //inverts contract documents by stemmed word
    private HashMap<String, ContractsList> invertedIndex = new HashMap<String, ContractsList>();

    /**
     * Index the given contract
     * @param contract
     */
    public void add(AwardedContract contract){
        String data = contract.getContractor() +" "+ contract.getDescriptionOfContract() + " " + contract.getParent();
        Set<TokenizedTerm> tokens = StringParsingUtils.tokenizeAndStem(data);
        for(TokenizedTerm term : tokens){
            if(!invertedIndex.containsKey(term.getStemmedTerm())){
                invertedIndex.put(term.getStemmedTerm(), new ContractsList());
            }
            invertedIndex.get(term.getStemmedTerm()).add(contract);
        }
    }

    public ContractsList queryFor(String stemmedTerm){
        ContractsList cl = invertedIndex.get(stemmedTerm);
        if(cl == null)
            return new ContractsList();
        return cl;
    }
}
