package fr.ensimag.deca;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.apache.log4j.Logger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl17
 * @date 01/01/2018
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("Nous sommes l'equipe 17");
        }
        if (options.getSourceFiles().isEmpty() && !options.getPrintBanner()) {
        	System.out.println(" Les options sont : ");
        	System.out.println(" -b  : displays a banner informing on the team name (needs to be used alone) / affiche une baniere indiquant le nom d'equipe( doit etre utiliser seul)");
        	System.out.println(" -p  : stops compilation after the tree construction and displays the corresponding deca program / s'arrete apres la construction de l'arbre et renvoie un programme deca correspondant à cet arbre");
        	System.out.println(" -v  : stops compilation after the verification step / ne fait que les etapes de verification");
        	System.out.println(" -n  : doesn't check for buffer overflows / ne fait pas les tests de debordement");
        	System.out.println(" -r X  : limits the number of general purpose registers to X / limite le fontionnement avec X registres banalisés");
        	System.out.println(" -d  : activates the debug trace (use several times for multiple traces) / active les traces de debug (plusieurs fois --> ajoute des traces)");
        	System.out.println(" -P  : launches parralel compilation in case of multiple source files / lance la compilation en parallele s'il ya plusieurs fichiers");
        }
        if (options.getParallel()) {
            int nbProc = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(nbProc);
            List<Future<Boolean>> res = new ArrayList<Future<Boolean>>();
            for (File source : options.getSourceFiles()) {
                Callable<Boolean> worker = new DecacCompiler(options, source);
                Future<Boolean> submit = executor.submit(worker);
                res.add(submit);
            }
            for (Future<Boolean> future : res) {
                try {
                  if(future.get()){
                    error = true;
                  }
                } catch (InterruptedException e) {
                    System.err.println("Interruption during parallel compilation");
                } catch (ExecutionException e) {
                    System.err.println("Exception during parallel compilation");
                }
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
