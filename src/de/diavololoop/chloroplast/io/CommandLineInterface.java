package de.diavololoop.chloroplast.io;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by gast2 on 26.09.17.
 */
public class CommandLineInterface {

    private DiscoMaker discoMaker;

    public CommandLineInterface(DiscoMaker discoMaker){
        this.discoMaker = discoMaker;

        Thread inputThread = new Thread(this::inputLoop);
        inputThread.start();
    }

    private void inputLoop() {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

            System.out.print("> ");

            while(!Thread.currentThread().isInterrupted()){
                String cmd[] = reader.readLine().split(" ");

                if(cmd[0].equalsIgnoreCase("help")){
                    System.out.println("commands:");
                    System.out.println("\thelp                   | prints this help");
                    System.out.println("\tlist                   | lists all available effects");
                    System.out.println("\teffect name [args]     | select the effect with given name ");
                    System.out.println("\tcurrent                | displays current effect");
                    System.out.println("\treload                 | reload the effects directory");
                    System.out.println("\tquit                   | exits the program");
                    System.out.print("\r\n> ");
                }else if(cmd[0].equalsIgnoreCase("list")){
                    System.out.println("\r\ncurrent loaded effects:");
                    System.out.printf("\t%-16s | %-32s | %-16s\r\n\t", "name", "description", "author");
                    for(int i = 0; i < 16 + 32 + 16 + 2*3; ++i){
                        System.out.print('-');
                    }
                    System.out.println();
                    discoMaker.getEffectLoader().allEffects().values().forEach(effect -> System.out.printf("\t%-16s | %-32s | %-16s\r\n", effect.getName(), effect.getDescription(), effect.getAuthor()));
                    System.out.print("\r\n> ");
                }else if(cmd[0].equalsIgnoreCase("effect") && cmd.length >= 2){
                    String effectName = cmd[1];
                    String args = "";
                    if(cmd.length >= 3){
                        args = cmd[2];
                    }

                    Effect effect = discoMaker.getEffectLoader().allEffects().get(effectName);

                    if(effect == null){
                        System.out.println("Effect "+effectName+" does not exists");
                    }else{
                        System.out.println("set Effect to " + effect.getName() + "#"+args);
                        discoMaker.setEffect(effect, args);
                    }
                    System.out.print("> ");
                }else if(cmd[0].equalsIgnoreCase("current")){
                    System.out.println("current Effect:");
                    System.out.println("\tname:        "+discoMaker.getEffect().getName());
                    System.out.println("\tdescription: "+discoMaker.getEffect().getDescription());
                    System.out.println("\tautor:       "+discoMaker.getEffect().getAuthor());
                    System.out.println("\tprefFps:     "+discoMaker.getEffect().getPreferedFPS());
                    System.out.print("> ");
                }else if(cmd[0].equalsIgnoreCase("reload")){
                    discoMaker.getEffectLoader().loadEffects();
                    System.out.print("> ");
                }else if(cmd[0].equalsIgnoreCase("quit")){
                    discoMaker.exit();
                }

            }
        } catch (IOException e) {
            discoMaker.exit();
        }

    }


}
