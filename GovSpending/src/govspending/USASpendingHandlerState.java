package govspending;

/**
 * Flags indicating node traversal state when parsing a USASpending.gov XML document.
 * @author Jarret
 */
public class USASpendingHandlerState {
    private boolean inObligatedAmount = false;
    private boolean inSignedDate = false;
    private boolean inVendorName = false;
    private boolean inParentName = false;
    private boolean inDescriptionOfContractRequirement = false;

    /**
     * @return the inObligatedAmount
     */
    public boolean isInObligatedAmount() {
        return inObligatedAmount;
    }

    /**
     * @param inObligatedAmount the inObligatedAmount to set
     */
    public void setInObligatedAmount(boolean inObligatedAmount) {
        this.inObligatedAmount = inObligatedAmount;
    }

    /**
     * @return the inSignedDate
     */
    public boolean isInSignedDate() {
        return inSignedDate;
    }

    /**
     * @param inSignedDate the inSignedDate to set
     */
    public void setInSignedDate(boolean inSignedDate) {
        this.inSignedDate = inSignedDate;
    }

    /**
     * @return the inVendorName
     */
    public boolean isInVendorName() {
        return inVendorName;
    }

    /**
     * @param inVendorName the inVendorName to set
     */
    public void setInVendorName(boolean inVendorName) {
        this.inVendorName = inVendorName;
    }

    /**
     * @return the inParentName
     */
    public boolean isInParentName() {
        return inParentName;
    }

    /**
     * @param inParentName the inParentName to set
     */
    public void setInParentName(boolean inParentName) {
        this.inParentName = inParentName;
    }

    /**
     * @return the inDescriptionOfContractRequirement
     */
    public boolean isInDescriptionOfContractRequirement() {
        return inDescriptionOfContractRequirement;
    }

    /**
     * @param inDescriptionOfContractRequirement the inDescriptionOfContractRequirement to set
     */
    public void setInDescriptionOfContractRequirement(boolean inDescriptionOfContractRequirement) {
        this.inDescriptionOfContractRequirement = inDescriptionOfContractRequirement;
    }
}
