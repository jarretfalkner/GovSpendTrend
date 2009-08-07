package govspending;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central data store of contract records.
 * @author Jarret
 */
public class ContractsManager {
    //count every document
    private AtomicInteger totalLoaded = new AtomicInteger();
    //invert data by year
    private TreeMap<Integer,ContractsIndex> indexByYear = new TreeMap<Integer, ContractsIndex>();

    /**
     * Process a list of contracts
     * @param year
     * @param contracts
     */
    public void addContracts(int year, List<AwardedContract> contracts){
        if(!indexByYear.containsKey(year)){
            indexByYear.put(year, new ContractsIndex());
        }
        ContractsIndex idx = indexByYear.get(year);
        for(AwardedContract contract : contracts){
            idx.add(contract);
        }
        totalLoaded.addAndGet(contracts.size());
    }

    public int getTotalLoaded(){
        return totalLoaded.get();
    }

    /**
     * Search the indices for the given terms
     * @param terms
     * @return
     */
    public List<YearQueryResult> queryFor(String terms){
        List<YearQueryResult> results = new ArrayList<YearQueryResult>();
        Set<TokenizedTerm> tokens = StringParsingUtils.tokenizeAndStem(terms);
        for(int year : indexByYear.keySet()){
            for(TokenizedTerm token : tokens){
                results.add(new YearQueryResult(year, token.getOriginalTerm(), indexByYear.get(year).queryFor(token.getStemmedTerm())));
            }
        }
        return results;
    }

    /**
     * Format a google chart URL call for the given query term
     * @param terms
     * @return
     */
    public String googleChartFor(String terms){
        Set<TokenizedTerm> tokens = StringParsingUtils.tokenizeAndStem(terms);
        StringBuilder url = new StringBuilder("http://chart.apis.google.com/chart?cht=lxy&amp;chs=500x200&amp;chd=t:");
        
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = 0;
        int maxY = Integer.MIN_VALUE;
        for(TokenizedTerm token : tokens){
            StringBuilder xaxis = new StringBuilder();
            StringBuilder yaxis = new StringBuilder();
            for(int year : indexByYear.keySet()){
                if(xaxis.length() != 0){
                    xaxis.append(",");
                    yaxis.append(",");
                }
                xaxis.append(Integer.toString(year));
                minX = Math.min(minX, year);
                maxX = Math.max(maxX, year);
                int totVal = indexByYear.get(year).queryFor(token.getStemmedTerm()).getTotalValue();
                yaxis.append(Integer.toString(totVal));
                minY = Math.min(minY, totVal);
                maxY = Math.max(maxY, totVal);
            }
            url.append(xaxis);
            url.append("|");
            url.append(yaxis);
            url.append("|");
        }
        url.deleteCharAt(url.length()-1);

        //set bounds per dataset
        url.append("&amp;chds=");
        StringBuilder bounds = new StringBuilder();
        for(TokenizedTerm token : tokens){
            if(bounds.length() != 0){
                bounds.append(",");
            }
            bounds.append(Integer.toString(minX));
            bounds.append(",");
            bounds.append(Integer.toString(maxX));
            bounds.append(",");
            bounds.append(Integer.toString(minY));
            bounds.append(",");
            bounds.append(Integer.toString(maxY));
        }

        url.append(bounds.toString());

        //draw axes
        url.append("&amp;chxt=x,y,x");
        url.append("&amp;chxl=0:");
        for(int year : indexByYear.keySet()){
                url.append("|");
            url.append(Integer.toString(year));
        }
        url.append("|1:");
        url.append("|0");
        url.append("|");
        url.append("$");
        url.append(Integer.toString(maxY/2));
        url.append("M|");
        url.append("$");
        url.append(Integer.toString(maxY));
        url.append("M|2:|||Fiscal Year|||");

        //make legend
        url.append("&amp;chdl=");

        boolean firstLegend = true;
        for(TokenizedTerm token : tokens){
            if(!firstLegend){
                url.append("|");
            }
            url.append(token.getOriginalTerm());
            firstLegend = false;
        }

        //and colors
        url.append("&amp;chco=FF0000,00FF00,0000FF");

        return url.toString();

    }
}
