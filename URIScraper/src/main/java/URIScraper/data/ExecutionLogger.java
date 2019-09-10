package URIScraper.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author Mauro
 */
public class ExecutionLogger {
    
    public static String langSlug = "PT";
    private static ExecutionLogger instance = null;
    
    public static ExecutionLogger getInstance(){
        if (instance == null) instance = new ExecutionLogger();
        return instance;
    }
    
    private final String EXPORT_PATH;
    private final StringBuilder stringBuilder;
    
    private ExecutionLogger(){
        stringBuilder = new StringBuilder();
        EXPORT_PATH = System.getProperty("user.home") 
                    + File.separator + "URIScraper_" + langSlug.toUpperCase() + File.separator;
        
        DateFormat df = DateFormat.getDateTimeInstance();
        stringBuilder.append("URIScraper (Error) Log File")
                .append("\nDate : ")
                .append(df.format(new Date()))
                .append("\nSelected language : ")
                .append(langSlug.toLowerCase())
                .append("\n-----------------------------\n\n");
    }
    
    synchronized public void appendMessage(String msg){
        stringBuilder.append(msg);
    }
    
    synchronized public void appendMessage(Exception e){
        stringBuilder.append(String.format("\nMessage: %s"
                            + "\nFull error: %s"
                            + "\n------------------------------------\n",
                            e.getLocalizedMessage(), e.toString()));
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    synchronized public boolean saveToFile(){
        try {
            Files.writeString(Paths.get(EXPORT_PATH + "LOG.txt"), stringBuilder.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
