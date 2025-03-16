package pledge;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 *
 * @author Christopher Henard
 */
public class CommandLineParser {

    private JCommander jCommander;
    private Generate commandGenerate;
    private Prioritize commandPrioritize;
    private String[] args;
    public static final String GENERATE = "generate_products";
    public static final String PRIORITIZE = "prioritize_products";

    public CommandLineParser(String[] args, String programName) {
        this.args = args;

        commandGenerate = new Generate();
        commandPrioritize = new Prioritize();
        jCommander = new JCommander();
        jCommander.addCommand(GENERATE, commandGenerate);
        jCommander.addCommand(PRIORITIZE, commandPrioritize);
        jCommander.setProgramName("java -jar " + programName + ".jar");

    }

    @Parameters(commandDescription = "Generate products")
    public class Generate {

        @Parameter(names = "-fm", description = "Feature model (SPLOT format by default)", required = true)
        public String fmFile;
        @Parameter(names = "-o", description = "Output file", required = true)
        public String outputFile;
        @Parameter(names = "-nbProds", description = "Number of products to generate.")
        public int nbProds = 10;
        @Parameter(names = "-timeAllowedMS", description = "Time allowed for the generation in ms")
        public long timeAllowed = 60000;
        @Parameter(names = "-dimacs", description = "Specify if the FM is a dimacs one")
        public boolean dimacs = false;
    }
    
    @Parameters(commandDescription = "Prioritize products")
    public class Prioritize {

        @Parameter(names = "-i", description = "Input products file", required = true)
        public String inputFile;
        
        @Parameter(names = "-t", description = "technique (greedy or nearoptimal)", required = true)
        public String technique;
        
        @Parameter(names = "-o", description = "Output file", required = true)
        public String outputFile;

    }

    public Generate getCommandGenerate() {
        return commandGenerate;
    }

    public void parseArgs() {
        jCommander.parse(args);
    }

    public String getCommandName() {
        return jCommander.getParsedCommand();
    }

    public void printUsage() {
        jCommander.usage();
    }
}
