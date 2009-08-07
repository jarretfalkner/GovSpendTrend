package govspending;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses USASpending.gov XML data and notifies a listener of awarded contracts
 * 
 * @author Jarret
 */
public class USASpendingHandler extends DefaultHandler{
    private USASpendingHandlerState parserState = new USASpendingHandlerState();
    private USASpendingAwardedContractHandler handler;
    private AwardedContract currentParse = null;
    private StringBuilder currentString = null;

    //interesting nodes in the XML
    private static final String RECORD_ELEMENT = "record";
    private static final String OBLIGATED_AMOUNT_ELEMENT = "obligatedAmount";
    private static final String DESCRIPTION_OF_CONTRACT_ELEMENT = "descriptionOfContractRequirement";
    private static final String SIGNED_DATE_ELEMENT = "signedDate";
    private static final String VENDOR_NAME_ELEMENT = "vendorName";
    private static final String PARENT_NAME_ELEMENT = "mod_parent";

    public USASpendingHandler(USASpendingAwardedContractHandler contractHandler){
        this.handler = contractHandler;
    }

    public void characters(char[] ch, int start, int length){
        if(currentString != null){
            currentString.append(ch, start, length);
        }
    }

    public void error(SAXParseException e)
           throws SAXException{
        System.err.println("parsing exception");
        e.printStackTrace();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(isRecordElement(localName, qName)){
            currentParse = new AwardedContract();
        } else if(isObligatedAmountElement(localName, qName)){
            parserState.setInObligatedAmount(true);
            currentString = new StringBuilder();
        } else if(isDescriptionOfContractElement(localName, qName)){
            parserState.setInDescriptionOfContractRequirement(true);
            currentString = new StringBuilder();
            
        } else if(isSignedDateElement(localName, qName)){
            parserState.setInSignedDate(true);
            currentString = new StringBuilder();
            
        } else if(isVendorNameElement(localName, qName)){
            parserState.setInVendorName(true);
            currentString = new StringBuilder();
            
        } else if(isParentNameElement(localName, qName)){
            parserState.setInParentName(true);
            currentString = new StringBuilder();
        }

    }

    public void endElement(String uri, String localName, String qName) {
        if(isRecordElement(localName, qName)){
            handler.contractParsed(currentParse);
            currentParse = null;
        } else if(isObligatedAmountElement(localName, qName)){
            parserState.setInObligatedAmount(false);

            currentParse.setHowMuch((int)Double.parseDouble(currentString.toString()));
            currentString = null;

        } else if(isDescriptionOfContractElement(localName, qName)){
            parserState.setInDescriptionOfContractRequirement(false);
            currentParse.setDescriptionOfContract(currentString.toString());
            currentString = null;

        } else if(isSignedDateElement(localName, qName)){
            parserState.setInSignedDate(false);
            currentParse.setSignedDate(currentString.toString());
            currentString = null;

        } else if(isVendorNameElement(localName, qName)){
            parserState.setInVendorName(false);
            currentParse.setContractor(currentString.toString());
            currentString = null;

        } else if(isParentNameElement(localName, qName)){
            parserState.setInParentName(false);
            currentParse.setParent(currentString.toString());
            currentString = null;
        }
    }

    private boolean isDescriptionOfContractElement(String localName, String qName) {
        return isElement(localName, qName, DESCRIPTION_OF_CONTRACT_ELEMENT);
    }

    private boolean isObligatedAmountElement(String localName, String qName) {
        return isElement(localName, qName, OBLIGATED_AMOUNT_ELEMENT);
    }

    private boolean isParentNameElement(String localName, String qName) {
        return isElement(localName, qName, PARENT_NAME_ELEMENT);
    }

    private boolean isRecordElement(String localName, String qName){
        return isElement(localName, qName, RECORD_ELEMENT);
    }

    private boolean isElement(String localName, String qName, String canonicalName){
        return StringUtils.equalsIgnoreCase(localName, canonicalName) ||
                StringUtils.equalsIgnoreCase(qName, canonicalName);
    }

    private boolean isSignedDateElement(String localName, String qName) {
        return isElement(localName, qName, SIGNED_DATE_ELEMENT);
    }

    private boolean isVendorNameElement(String localName, String qName) {
        return isElement(localName, qName, VENDOR_NAME_ELEMENT);
    }
}
