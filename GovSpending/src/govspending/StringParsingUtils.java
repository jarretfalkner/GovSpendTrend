package govspending;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.miscellaneous.SingleTokenTokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 *
 * @author Jarret
 */
public class StringParsingUtils {
    private static Set<String> stopWords = null;
    private static Set<String> getStopWords(){
        if(stopWords != null){
            return stopWords;
        }
        stopWords = new HashSet<String>();
        stopWords.add("and");
        stopWords.add("the");
        stopWords.add("a");
        stopWords.add("an");
        for(int digitWord = 0; digitWord < 10; digitWord++){
            stopWords.add(Integer.toString(digitWord));
        }
        return stopWords;
    }

    /**
     * Tokenize and stem the given query string.
     * @param data
     * @return a unique set of tokens in the query
     */
    public static LinkedHashSet<TokenizedTerm> tokenizeAndStem(String data){
        LinkedHashSet<TokenizedTerm> tokens = new LinkedHashSet<TokenizedTerm>();
        StringReader reader = new StringReader(data);
        StandardTokenizer tokenizer = new StandardTokenizer(reader);
        LowerCaseFilter lower = new LowerCaseFilter(tokenizer);
        StopFilter stopFilter = new StopFilter(lower, getStopWords());
        //PorterStemFilter filter = new PorterStemFilter(lower);
        Token t = new Token();
        while(t != null){
            String term = t.term();
            try {
                if(StringUtils.isNotBlank(term)){
                    String stemmed = new PorterStemFilter(new SingleTokenTokenStream(t)).next(t).term();
                    tokens.add(new TokenizedTerm(term, stemmed));
                }
                t = stopFilter.next(t);
            } catch (IOException ex) {
                t = null;
                Logger.getLogger(ContractsIndex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tokens;
    }
}
