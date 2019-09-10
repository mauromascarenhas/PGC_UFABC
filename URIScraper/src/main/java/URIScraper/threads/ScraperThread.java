/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package URIScraper.threads;

/**
 *
 * @author Mauro
 */
public abstract class ScraperThread extends Thread{
    
    private boolean finished;

    public ScraperThread() {
        finished = false;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    public boolean isFinished() {
        return finished;
    }
}
