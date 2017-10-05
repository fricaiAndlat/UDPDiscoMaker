package de.diavololoop.chloroplast;

import de.diavololoop.chloroplast.color.ColorModel;
import de.diavololoop.chloroplast.config.Config;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.io.EffectLoader;
import de.diavololoop.chloroplast.io.Sender;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author Chlrooplast
 *
 * Disco maker ist the core class.
 * Its responsible for controlling:
 * - The Sender, which sends the data to the LED-Stripes
 * - The EffectLoader, loading all Effects and provide them by name
 * - the EffectPlayer, using the effects for creating color data
 *
 */
public class DiscoMaker {

    private boolean isReady = false;

    private Sender sender;
    private EffectLoader effectLoader;
    private Effect currentEffect;
    private EffectPlayer player;
    private Config config;

    //hooks for modules
    private Consumer<Effect> onEffectChanged;
    private Runnable onQuit;
    private Runnable onReload;


    /**
     * in order to start the program its necessary to create an instace of this class. However, to be functional the
     * init method must be called exact one time before using other mothods.
     *
     * @param root the runtime root, where to search other files like Effects or config files.
     * @param configuration the configuration of all leds and other settings
     * @param effectChanged Callback if a effect is changed. Receives the newly assigned Effcet
     * @param onQuit Callback for the program should exit
     * @param onReload Callback for the reload action
     *
     * @throws IllegalStateException throws an exception when init is called more than once
     */
    public void init(File root, Config configuration, Consumer<Effect> effectChanged, Runnable onQuit, Runnable onReload){
        if(isReady){
            throw new IllegalStateException("can only init one time");
        }
        isReady = true;

        this.config = configuration;
        this.onEffectChanged = effectChanged;
        this.onQuit = onQuit;
        this.onReload = onReload;


        try {
            sender = new Sender(configuration);
        } catch (IOException e) {
            System.err.println("cant create Sender: "+e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }

        effectLoader = new EffectLoader(root);
        effectLoader.loadEffects();

        currentEffect = effectLoader.allEffects().get("SimpleColor");
        currentEffect.init("black", configuration.getPositions());

        player = new EffectPlayer(sender, currentEffect, config.getPositions().size() * ColorModel.maxByteLength());

    }


    /**
     * using the EffectLoader its easy to find Effects
     *
     * @return the current EffectLoader
     *
     * @throws IllegalArgumentException throws an exception when init is not called
     */
    public EffectLoader getEffectLoader(){
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        return effectLoader;
    }

    /**
     * Sets and play a given Effect.
     * This method is Thread Safe
     *
     * @param effect the Effect next to play
     * @param args optional arguments for the Effect for Example the color or speed
     */
    public synchronized void setEffect(Effect effect, String args){
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        if(currentEffect != null){
            currentEffect.kill();
        }
        effect.init(args, config.getPositions());
        currentEffect = effect;
        player.play(effect);
        onEffectChanged.accept(effect);
    }

    /**
     *
     * @return the current setted Effect
     *
     * @throws IllegalArgumentException throws an exception when init is not called
     */
    public Effect getEffect(){
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        return currentEffect;
    }

    /**
     * Shutdown the Project and closes all open servers
     *
     * @throws IllegalArgumentException throws an exception when init is not called
     */
    public void exit() {
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        onQuit.run();

        if(currentEffect != null){
            currentEffect.kill();
        }

        currentEffect = effectLoader.allEffects().get("SimpleColor");
        currentEffect.init("black", config.getPositions());
        player.play(currentEffect);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        player.stop();
        System.exit(0);
    }

    /**
     * @return the Config the program is started with
     *
     * @throws IllegalArgumentException throws an exception when init is not called
     */
    public Config getConfig() {
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        return config;
    }

    /**
     * reload all resources, except the Config
     *
     * @throws IllegalArgumentException throws an exception when init is not called
     */
    public void reload(){
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        effectLoader.loadEffects();
        onReload.run();
    }
}
