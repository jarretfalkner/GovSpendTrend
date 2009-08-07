/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package govspending.listeners;

import govspending.AwardedContract;
import govspending.ContractsManager;
import govspending.USASpendingAwardedContractHandler;
import govspending.USASpendingHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;

/**
 * Web application lifecycle listener to load the index of government spending
 * data.
 * @author Jarret
 */

public class DataLoaderApplicationListener implements ServletContextListener {
    public static final String DATA_LOADER_CONTEXT_KEY = "data loader";

    ExecutorService ex = Executors.newFixedThreadPool(4);
    final boolean[] alive = new boolean[]{true};

    public void contextInitialized(ServletContextEvent sce) {
        final ContractsManager contractManager = new ContractsManager();
        for(final File f : new File("/opt/govspending/").listFiles()){
            ex.submit(new Runnable(){

                public void run() {
                    //exit if the app was unloaded.
                    if(!alive[0]){
                        return;
                    }

                    //read the file
                    InputStream is = null;
                    InputStreamReader isr = null;
                    BufferedReader br = null;
                    try{
                        final List<AwardedContract> contracts = new ArrayList();
                        is = new FileInputStream(f);
                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);

                        //xml parsing
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser saxParser =  factory.newSAXParser();
                        saxParser.parse(new InputSource(br), new USASpendingHandler(new USASpendingAwardedContractHandler() {
                            public void contractParsed(AwardedContract handleMe) {
                                contracts.add(handleMe);
                            }
                        }));

                        //lock on the contract manager -- it's not synchronized internally to be thread safe.
                        synchronized(contractManager){
                            contractManager.addContracts(Integer.parseInt(f.getName().split("-")[0]), contracts);
                        }

                        System.out.println(contracts.size() + " contracts parsed in " + f.getName());
                    } catch (Exception e){
                        System.err.println("can't parse " + f);
                        e.printStackTrace();
                    } finally {
                        //cleanup
                        IOUtils.closeQuietly(br);
                        IOUtils.closeQuietly(isr);
                        IOUtils.closeQuietly(is);
                    }
                }
            });

        }
        sce.getServletContext().setAttribute(DATA_LOADER_CONTEXT_KEY, contractManager);
        ex.shutdown();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //flag any runnables to exit without processing data
        alive[0] = false;
        try{
            ex.shutdownNow();
        } catch (Exception e){
            //noop
        }
        sce.getServletContext().removeAttribute(DATA_LOADER_CONTEXT_KEY);
    }
}