package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import fr.ensimag.deca.RegisterManager;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParse(){
        return parse;
    }

    public boolean getVerif(){
        return verif;
    }

    public boolean getNoCheck(){
      return this.noCheck;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public RegisterManager getRegisterManager(){
      return this.registerManager;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verif = false;
    private boolean noCheck = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private RegisterManager registerManager = new RegisterManager(15,2);


    public void parseArgs(String[] args) throws CLIException {

        int parc_args = 0;

	      if(!(args.length == 0)){
           while ((args.length - parc_args) != 0){
             switch (args[parc_args]) {
             case "-b":
                 if(args.length != 1){
                   throw new CLIException("L'option -b doit etre utilisée seule ");
                 }
                 printBanner = true;
                 parc_args++;
                 break; // keep default
             case "-p":
                 if(verif == true){
                   throw new CLIException("L'option -p ne peut pas etre utilisée avec l'option -v");
                 }
                 parse = true;
                 parc_args++;
                 break;
             case "-v":
                 if(parse == true){
                   throw new CLIException("L'option -v ne peut pas etre utilisée avec l'option -p");
                 }
                 verif = true;
                 parc_args++;
                 break;
             case "-r":
                 parc_args++;
                 int nbRegister = 0;
                  try{
                      nbRegister = Integer.parseInt(args[parc_args]);
                  }
                  catch(NumberFormatException e){
                      throw new CLIException("L'option -r requiert un nombre de registre en décimal");
                  }
                 if (4 > nbRegister || nbRegister > 16){
                   throw new CLIException("L'option -r requiert un nombre de registre entre 4 et 16");
                 }
                 this.registerManager.setRegisterMax(nbRegister);
                 parc_args++;
                 break;
             case "-n":
                 this.noCheck = true;
                 parc_args++;
                 break;
             case "-d":
                  debug++;
                  parc_args++;
                  break;
             case "-P":
                  parallel = true;
                  parc_args++;
                  break;
             default:
                 File source = new File(args[parc_args]);
                 sourceFiles.add(source);
                 parc_args++;
                 break;
             }
           }
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

    }

    protected void displayUsage() {
        System.out.println("decac s'utilise comme ceci : ");
        System.out.println("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
    }
}
