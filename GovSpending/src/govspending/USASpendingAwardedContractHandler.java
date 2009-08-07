package govspending;

/**
 * Defines a callback to handle each record parsed out of USASpending.gov XML
 * documents.
 * @author Jarret
 */
public interface USASpendingAwardedContractHandler {
    public void contractParsed(AwardedContract handleMe);
}
