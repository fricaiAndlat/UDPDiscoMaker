package de.diavololoop.chloroplast.modules;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;

import java.io.File;

public abstract class Module {

    private final DiscoMaker discoMaker;
    private boolean loaded;


    public Module(DiscoMaker discoMaker){
        this.discoMaker = discoMaker;
    }

    protected DiscoMaker program(){
        return discoMaker;
    }

    protected final void markLoaded(){
        loaded = true;
    }
    public final boolean isLoaded(){
        return loaded;
    }

    public abstract String getKey();
    public abstract int getParameterCount();
    public abstract String init(String[] args);
    public abstract String onStart(File root);

    public void onEffectChange(Effect effect){}
    public void onQuit(){}
    public void onReaload(){}
}
