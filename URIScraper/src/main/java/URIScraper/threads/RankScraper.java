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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author mauro.mascarenhas
 */
public class RankScraper extends ScraperThread {
    private final URIProblem problem;
    
    public RankScraper(URIProblem problem) {
        this.problem = problem;
    }

    @Override
    public void run() {
        Document doc;
        try{
            String lang = this.problem.getDescriptionLanguage();
            doc = Jsoup.connect(String.format("https://www.urionlinejudge.com.br/judge/%s/ranks/problem/%s/2" ,
                    lang.isEmpty() ? "pt" : lang.substring(1).toLowerCase(), this.problem.getProblemID())).get();
            
            Element statisticDiv = doc.getElementById("problem-statistics");
            Elements cElement = statisticDiv.getElementsByTag("dd");
            if (cElement.size() == 4){
                this.problem.setStatisticLevel(cElement.get(0).wholeText().trim());
                this.problem.setStatisticSubmissions(Integer.parseInt(cElement.get(1).wholeText().trim().replaceAll("[\\x00-\\x2F\\x3A-\\xFF]", "")));
                this.problem.setStatisticSolved(Integer.parseInt(cElement.get(2).wholeText().trim().replaceAll("[\\x00-\\x2F\\x3A-\\xFF]", "")));
                this.problem.setStatisticRatio(cElement.get(3).wholeText().trim());
            }
        } catch (IOException e) {
            ExecutionLogger.getInstance().appendMessage("\n[ RANK ]"
                    + "\nURI Problem: " + problem.getProblemID());
            ExecutionLogger.getInstance().appendMessage(e);
        } finally{
            setFinished(true);
        }
    }
}
