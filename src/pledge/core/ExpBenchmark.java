package pledge.core;

public class ExpBenchmark {
    public String fmName;

    public long timeAllowed;

    public int[] sizes;

    public ExpBenchmark(String fmName, long timeAllowed, int[] sizes)
    {
        this.fmName = fmName;
        this.timeAllowed = timeAllowed;
        this.sizes = sizes;
    }
}
