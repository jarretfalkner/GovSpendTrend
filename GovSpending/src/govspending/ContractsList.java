package govspending;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a list of contracts and the total dollar value (in millions) of that
 * list.  These lists typically share some term, so the value of that term equals
 * the value of this list.
 * @author Jarret
 */
public class ContractsList {
    private List<AwardedContract> contracts = new ArrayList<AwardedContract>();
    private int totalValue = 0;

    public void add(AwardedContract contract){
        contracts.add(contract);
        totalValue+=contract.getHowMuch();
    }

    public int getTotalValue(){
        return totalValue;
    }
    public List<AwardedContract> getContracts(){
        return contracts;
    }

    public String toString(){
        return Integer.toString(totalValue) + " value for "+Integer.toString(contracts.size())+" contracts: " + contracts.toString();
    }
}
