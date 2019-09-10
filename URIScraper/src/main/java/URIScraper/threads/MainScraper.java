/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package URIScraper.threads;

import URIScraper.data.ExecutionLogger;
import URIScraper.data.URIProblem;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Mauro
 */
public class MainScraper extends ScraperThread{

    private final URIProblem problem;
    
    public MainScraper(URIProblem problem) {
        this.problem = problem;
    }

    @Override
    public void run() {
        Document doc;
        try{
            doc = Jsoup.connect(String.format("https://www.urionlinejudge.com.br/repository/UOJ_%s%s.html" ,
                    this.problem.getProblemID(), this.problem.getDescriptionLanguage())).get();
            
            boolean hasImages = false;
            
            Elements cElement = doc.getElementsByClass("description");
            if (cElement.size() > 0){
                this.problem.setDescription(cElement.get(0).wholeText().trim());
                hasImages |= cElement.get(0).getElementsByTag("img").size() > 0;
            }
            
            cElement = doc.getElementsByClass("input");
            if (cElement.size() > 0){
                this.problem.setInput(cElement.get(0).wholeText().trim());
                hasImages |= cElement.get(0).getElementsByTag("img").size() > 0;
            }
            
            cElement = doc.getElementsByClass("output");
            if (cElement.size() > 0){
                this.problem.setOutput(cElement.get(0).wholeText().trim());
                hasImages |= cElement.get(0).getElementsByTag("img").size() > 0;
            }
            
            this.problem.setExistentImage(hasImages);            
        } catch (IOException e) {
            ExecutionLogger.getInstance().appendMessage("\n[ MAIN ]"
                    + "\nURI Problem: " + problem.getProblemID());
            ExecutionLogger.getInstance().appendMessage(e);
        } finally{
            setFinished(true);
        }
    }
}
