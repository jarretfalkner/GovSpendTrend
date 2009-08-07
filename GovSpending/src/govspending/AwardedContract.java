package govspending;

/**
 * Information in each record of an awarded contract
 * @author Jarret
 */
public class AwardedContract {
    private String parent;
    private String contractor;
    private String descriptionOfContract;
    private String signedDate;
    private int howMuch;

    /**
     * Memory representation of USA spending contract records.
     * @param howMuch
     * @param descriptionOfContract
     * @param contractor
     * @param parent
     */
    public AwardedContract(int howMuch, String descriptionOfContract, String contractor, String parent){
        setHowMuch(howMuch);
        this.descriptionOfContract = descriptionOfContract;
        this.contractor = contractor;
        this.parent = parent;
    }

    /**
     * No-op constructor.  Call the setters to make reasonable state in the object.
     */
    public AwardedContract(){
    }

    /**
     * @return the parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * @return the contractor
     */
    public String getContractor() {
        return contractor;
    }

    /**
     * @return the descriptionOfContract
     */
    public String getDescriptionOfContract() {
        return descriptionOfContract;
    }

    /**
     * @return the howMuch
     */
    public int getHowMuch() {
        return howMuch;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * @param contractor the contractor to set
     */
    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    /**
     * @param descriptionOfContract the descriptionOfContract to set
     */
    public void setDescriptionOfContract(String descriptionOfContract) {
        this.descriptionOfContract = descriptionOfContract;
    }

    /**
     * @param howMuch the howMuch to set
     */
    public void setHowMuch(int howMuch) {
        this.howMuch = Math.max(howMuch, 0)/1000000;
    }

    public void setSignedDate(String toString) {
        signedDate = toString;
    }
    public String getSignedDate(){
        return signedDate;
    }
    public String toString(){
        return descriptionOfContract + " " + contractor + " " + parent+ "\n";
    }
}
