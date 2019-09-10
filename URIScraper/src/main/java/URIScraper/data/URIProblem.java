package URIScraper.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

/**
 *
 * @author mauro.mascarenhas
 */
public final class URIProblem {
    
    private final String EXPORT_PATH;
    
    public static class ProblemContract{
        public static final String ID = "id";
        public static final String LANGUAGE = "language";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String INPUT = "input";
        public static final String OUTPUT = "output";
        public static final String HAS_IMAGES = "has_images";
        public static final String CATEGORY = "category";
        public static final String LEVEL = "level";
        
        public static final String STATISTICS = "statistics";
        
        public static class Statistics{
            public static final String LEVEL = "level";
            public static final String SUBMISSIONS = "submissions";
            public static final String SOLVED = "solved";
            public static final String RATIO = "ratio";
        }
    }
    
    private final String id;
    private final String langSlug;
            
    private final JSONObject data;
    private final JSONObject statistics;
    
    public URIProblem(String problemID, String langSlug){
        this.data = new JSONObject();
        this.statistics = new JSONObject();
        
        this.EXPORT_PATH = System.getProperty("user.home")
                + File.separator + "URIScraper_" + (langSlug.isEmpty() ? "PT" : langSlug).toUpperCase()
                + File.separator;
        
        this.id = problemID;
        this.langSlug = langSlug.isEmpty() ? "" : "_" + langSlug;
        
        this.setID(problemID);
        this.setName("undefined");
        this.setDescription("undefined");
        this.setInput("undefined");
        this.setOutput("undefined");
        this.setCategory("undefined");
        this.setExistentImage(false);
        this.setLevel(-1);
        this.setStatisticLevel("undefined");
        this.setStatisticSubmissions(-1);
        this.setStatisticSolved(-1);
        this.setStatisticRatio("undefined");
        
        this.data.put(ProblemContract.STATISTICS, statistics);
    }
    
    private void setID(String pID){
        data.put(ProblemContract.ID, pID);
    }
    
    synchronized public void setName(String pName){
        data.put(ProblemContract.NAME, pName);
    }
    
    synchronized public void setDescription(String pDesc){
        data.put(ProblemContract.DESCRIPTION, pDesc);
    }
    
    synchronized public void setInput(String pIn){
        data.put(ProblemContract.INPUT, pIn);
    }
    
    synchronized public void setOutput(String pOut){
        data.put(ProblemContract.OUTPUT, pOut);
    }
    
    synchronized public void setExistentImage(boolean hasImages){
        data.put(ProblemContract.HAS_IMAGES, hasImages);
    }
    
    synchronized public void setCategory(String pCategory){
        data.put(ProblemContract.CATEGORY, pCategory);
    }
    
    synchronized public void setLevel(int pLevel){
        data.put(ProblemContract.LEVEL, pLevel);
    }
    
    synchronized public void setStatisticLevel(String pSLevel){
        statistics.put(ProblemContract.Statistics.LEVEL, pSLevel);
    }
    
    synchronized public void setStatisticSubmissions(int pSSubmissions){
        statistics.put(ProblemContract.Statistics.SUBMISSIONS, pSSubmissions);
    }
    
    synchronized public void setStatisticSolved(int pSSolved){
        statistics.put(ProblemContract.Statistics.SOLVED, pSSolved);
    }
    
    synchronized public void setStatisticRatio(String pSRatio){
        statistics.put(ProblemContract.Statistics.RATIO, pSRatio);
    }
    
    public JSONObject getJSONObject(){
        return data;
    }
    
    public String getProblemID(){
        return this.id;
    }
    
    public String getDescriptionLanguage(){
        return this.langSlug;
    }
    
    public boolean saveToFile(){
        return saveToFile(this.EXPORT_PATH, data.getString(ProblemContract.ID) + ".json", false);
    }
    
    public boolean saveToFile(boolean readable){
        return saveToFile(this.EXPORT_PATH, data.getString(ProblemContract.ID) + ".json", readable);
    }
    
    public boolean sateToFile(String filePath, String fileName){
        return saveToFile(filePath, fileName, false);
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean saveToFile(String filePath, String fileName, boolean readable){
        try {
            Files.writeString(Paths.get(filePath + fileName),
                    readable ? getJSONObject().toString(4) : getJSONObject().toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }   
}
