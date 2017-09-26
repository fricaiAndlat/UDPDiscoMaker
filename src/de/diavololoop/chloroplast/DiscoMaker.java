package de.diavololoop.chloroplast;

import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.io.EffectLoader;

import java.io.File;

/**
 * Created by gast2 on 26.09.17.
 */
public class DiscoMaker {
//135
    Sender sender = new Sender();
    EffectLoader effectLoader;

    public DiscoMaker(){

        File root = new File("./");

        effectLoader = new EffectLoader(root);
        effectLoader.loadEffects();

    }

    public EffectLoader getEffectLoader(){
        return effectLoader;
    }

    public void setEffect(String name, String args){

    }
    public Effect getEffect(){
        return null;
    }


    public static void main(String[] args){

        int port = -1;

        for(int i = 0; i < args.length; ++i){
            if(args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--help")){
                printUsage();
                return;
            }else if(args[i].equalsIgnoreCase("-w")){

                if(args.length > i+1){
                    if(args[i+1].matches("\\d{1,5}")){
                        port = Integer.parseInt(args[i+1]);
                        ++i;
                    }else{
                        System.out.println("port our of range");
                        return;
                    }
                }else{
                    printUsage();
                    return;
                }

            }else{
                printUsage();
                return;
            }
        }


        new DiscoMaker();
    }
    public void exit() {


    }

    public static void printUsage(){
        System.out.println("usage <args>");
        System.out.println("\t-h       \tprints this help");
        System.out.println("\t-w <port>\tstarts a webinterface on given port");
    }


}
