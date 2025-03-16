package pledge.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sat4j.specs.TimeoutException;

public class TWiseCoverage {
    private int t;

    private String fmName;

    private long allowedSeconds;

    private int size;

    private int[] firstTSetNbs;

    private int[] finalTSetNbs;

    public TWiseCoverage(int t, String fmName, long allowedSeconds, int size)
    {
        this.t = t;
        this.fmName = fmName;
        this.allowedSeconds = allowedSeconds;
        this.size = size;
    }  

    public void updateFirstTSetsNb(List<Product> products)
    {
        firstTSetNbs = getTInteractionNbs(products);
    }

    public void updateFinalTSetsNb(List<Product> products)
    {
        finalTSetNbs = getTInteractionNbs(products);
    }

    public int[] getTInteractionNbs(List<Product> products)
    { 
        int[] tSetNbs = new int[t - 1];
        int currT = 2;
        while (currT <= t)
        {
            Set<TSet> tInteractions = new HashSet<>();  
            for (Product product : products) {
                try {
                    tInteractions.addAll(product.getCoveredTFeatures(currT));
                } catch (Exception e)
                {
                    tInteractions.clear();
                    break;
                }
            }
            if (tInteractions.size() == 0)
            {
                tSetNbs[currT - 2] = -1;
            }
            else
            {
                tSetNbs[currT - 2] = tInteractions.size();
            }
            currT++;
        }
        return tSetNbs;
    }

    public int getTSetNb(int t, boolean isFinal)
    {
        int[] tSetNbs = (isFinal) ? finalTSetNbs : firstTSetNbs;
        return tSetNbs[t - 2];
    }

    public void updateFile()
    {

    }
    
}
