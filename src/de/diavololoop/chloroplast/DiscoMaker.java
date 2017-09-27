package de.diavololoop.chloroplast;

import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.io.CommandLineInterface;
import de.diavololoop.chloroplast.io.EffectLoader;
import de.diavololoop.chloroplast.io.Sender;
import de.diavololoop.chloroplast.io.WebInterface;

import java.io.File;
import java.io.IOException;

/**
 * Created by gast2 on 26.09.17.
 */
public class DiscoMaker {

    private Sender sender = new Sender();
    private EffectLoader effectLoader;
    private Effect currentEffect;
    private EffectPlayer player;

    int nleds = 135;

    public DiscoMaker(File root){


        effectLoader = new EffectLoader(root);
        effectLoader.loadEffects();

        currentEffect = effectLoader.allEffects().get("SimpleColor");
        currentEffect.init(nleds, "black");

        player = new EffectPlayer(sender, currentEffect, nleds);

    }

    public EffectLoader getEffectLoader(){
        return effectLoader;
    }

    public void setEffect(Effect effect, String args){
        if(currentEffect != null){
            currentEffect.kill();
        }
        effect.init(nleds, args);
        currentEffect = effect;
        player.play(effect);
    }

    public Effect getEffect(){
        return currentEffect;
    }

    public void exit() {
        currentEffect = effectLoader.allEffects().get("SimpleColor");
        currentEffect.init(nleds, "black");
        player.play(currentEffect);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        player.stop();
        if(currentEffect != null){
            currentEffect.kill();
        }
        System.exit(0);
    }

    public static void main(String[] args){

        int port = -1;

        for(int i = 0; i < args.length; ++i){
            if(args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--help")){
                printUsage();
                return;
            } else if(args[i].equalsIgnoreCase("-w")){

                if(args.length > i+1){
                    if(args[i+1].matches("\\d{1,5}")){
                        port = Integer.parseInt(args[i+1]);
                        ++i;
                    }else{
                        System.out.println("port our of range");
                        return;
                    }
                } else {
                    printUsage();
                    return;
                }

            }else{
                printUsage();
                return;
            }
        }

        File root = new File("./");
        DiscoMaker discoMaker = new DiscoMaker(root);
        CommandLineInterface cmdInterface = new CommandLineInterface(discoMaker);

        try {
            WebInterface webInterface = new WebInterface(root, discoMaker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUsage(){
        System.out.println("usage <args>");
        System.out.println("\t-h       \tprints this help");
        System.out.println("\t-w <port>\tstarts a webinterface on given port");
    }


}
