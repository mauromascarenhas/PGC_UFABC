/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package URIScraper;

import URIScraper.data.ExecutionLogger;
import URIScraper.data.URIProblem;
import URIScraper.threads.MainScraper;
import URIScraper.threads.RankScraper;
import URIScraper.threads.ScraperThread;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mauro
 */
public class Main {

    private static ArrayList<ScraperThread> threads;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int option;
        String lang = "pt";
        Scanner in = new Scanner(System.in);
        
        do {
            System.out.println("Language index (valid options):\n"
                    + "0 - Portuguese;\n"
                    + "1 - English;\n"
                    + "2 - Spanish;\n"
                    + "Please, select the language of the URI problems :");
            option = in.nextInt();
            switch(option){
                case 0: lang = "pt"; break;
                case 1: lang = "en"; break;
                case 2: lang = "es"; break;
                default: lang = "undef"; break;
            }
        } while (option < 0 || option > 2);
        
        System.out.println("\nSelected language : " + lang + "\n");
        ExecutionLogger.langSlug = lang;
        
        threads = new ArrayList<>();
        
        try {
            final String EXPORT_PATH = System.getProperty("user.home") 
                    + File.separator + "URIScraper_" + lang.toUpperCase() + File.separator;
            
            Path appDir = Paths.get(EXPORT_PATH);
            if (!Files.exists(appDir)) Files.createDirectories(appDir);
        } catch (IOException ex) {
            ExecutionLogger.getInstance().appendMessage(ex);
        }
        
        Document doc;
        try {
            String nextURL = String.format("https://www.urionlinejudge.com.br/judge/%s/problems/all", lang);
            if (lang.equals("pt")) lang = "";
            
            System.out.print("Acquiring problem (ID) : 00000000000");
            
            while (true){
                doc = Jsoup.connect(nextURL).get();
                
                Elements probs = doc.getElementById("element").getElementsByTag("tr");
                for (int i = 1; i < probs.size(); ++i){
                    Element current = probs.get(i);
                    String problemId = current.getElementsByTag("td").get(0).wholeText().trim();
                    if (problemId.isEmpty()) break;
                    
                    for (int j = 0; j < problemId.length(); ++j) System.out.print("\b");
                    System.out.flush();
                    System.out.print(problemId);
                    
                    URIProblem problem = new URIProblem(problemId, lang);
                    problem.setName(current.getElementsByTag("td").get(2).wholeText().trim());
                    problem.setCategory(current.getElementsByTag("td").get(3).wholeText().trim());
                    problem.setLevel(Integer.parseInt(current.getElementsByTag("td").get(5).wholeText().trim()));
                    
                    threads.add(new MainScraper(problem));
                    threads.add(new RankScraper(problem));
                    
                    for (ScraperThread t : threads)
                        t.start();
                    
                    syncThreads();
                    problem.saveToFile();
                    threads.clear();
                }                
                if (doc.getElementsByClass("next disabled").size() > 0) break;
                nextURL = doc.getElementsByClass("next")
                        .get(0).getElementsByTag("a")
                        .get(0).absUrl("href");
            }
        } catch (IOException ex) {
            ExecutionLogger.getInstance().appendMessage(ex);
        }
        
        ExecutionLogger.getInstance().saveToFile();
        System.out.println("\n\nComplete! [Check the log file for details]");
    }
    
    private static void syncThreads(){
        boolean isFinished;
        do{
            isFinished = true;
            for (ScraperThread a: threads)
                isFinished &= a.isFinished();
        } while (!isFinished);
    }
}