/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package govspending;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Jarret
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, SAXException, ParserConfigurationException {
        URL u = new URL("http://www.usaspending.gov/fpds/fpds.php?fiscal_year=2004&stateCode=VA&sortby=f&datype=X&reptype=r&detail=3");

        ContractsManager contractManager = new ContractsManager();
        for(File f : new File("/opt/govspending/").listFiles()){
            final List<AwardedContract> contracts = new ArrayList();
            InputStream is = new FileInputStream(f);//u.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser =  factory.newSAXParser();
            saxParser.parse(new InputSource(br), new USASpendingHandler(new USASpendingAwardedContractHandler() {
                public void contractParsed(AwardedContract handleMe) {
                    contracts.add(handleMe);
                }
            }));
            br.close();
            isr.close();
            is.close();

            contractManager.addContracts(Integer.parseInt(f.getName().split("-")[0]), contracts);

            System.out.println(contracts.size() + " contracts parsed");
        }
        System.out.println(contractManager.queryFor("warfare"));
    }

}
