package de.diavololoop.chloroplast.modules;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;

import java.io.File;

/**
 * @author Chloroplast
 *
 * Modules are used to do specific tasks in the lifetime of the program, for example open a HTTPServer or a commandline
 * interface. MOdules can manage Program arguments.
 *
 */
public abstract class Module {

    private final DiscoMaker discoMaker;
    private boolean loaded;

    /**
     * Creates the module with a reference to the core program. the core Program is not ready at this time.
     *
     * @param discoMaker core program, not initialised yet
     */
    public Module(DiscoMaker discoMaker){
        this.discoMaker = discoMaker;
    }

    /**
     *
     * @return the reference to the core program
     */
    protected DiscoMaker program(){
        return discoMaker;
    }

    /**
     * must be called when the module is been initialised or is otherwise ready.
     */
    protected final void markLoaded(){
        loaded = true;
    }

    /**
     * returns if markLoaded was called previously
     *
     * @return load status
     */
    public final boolean isLoaded(){
        return loaded;
    }

    /**
     * getter for the key used in program arguments
     * for example -i for interactive or -http for the http server
     *
     * @return used key
     */
    public abstract String getKey();

    /**
     * number of parameter to be read after the key was found
     *
     * @return number of parameters
     */
    public abstract int getParameterCount();

    /**
     * method will be called when key was found.
     *
     * @param args the arguments for this module
     * @return if an error occured the description of the error or null otherwise
     */
    public abstract String init(String[] args);

    /**
     * method will be called after reading all program arguments.
     *
     * will not be called when markLoaded wasnt called before
     *
     *
     * @param root the program root directory.
     * @return if an error occured the description of the error or null otherwise
     */
    public abstract String onStart(File root);

    /**
     * will be called when the core program changes the Effect
     *
     * @param effect next Effect
     */
    public void onEffectChange(Effect effect){}

    /**
     * will be called before program exits. Should be used to release resources
     */
    public void onQuit(){}

    /**
     * will be called when a reload should be performed
     */
    public void onReaload(){}
}
