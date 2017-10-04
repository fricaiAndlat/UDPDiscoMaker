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
 * Created by gast2 on 26.09.17.
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



    public EffectLoader getEffectLoader(){
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        return effectLoader;
    }

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

    public Effect getEffect(){
        if(!isReady){
            throw new IllegalStateException("init must be called first");
        }
        return currentEffect;
    }

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

    public Config getConfig() {
        return config;
    }

    public void reload(){
        effectLoader.loadEffects();
        onReload.run();
    }
}
