package de.diavololoop.chloroplast.modules.cmdline;

import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.modules.Module;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Chloroplast
 *
 * Module for communicating over the standard input and output
 */
public class ModuleCommandline extends Module {

    public ModuleCommandline(DiscoMaker discoMaker) {
        super(discoMaker);
    }

    @Override
    public String getKey() {
        return "-i";
    }

    @Override
    public int getParameterCount() {
        return 0;
    }

    @Override
    public String init(String[] args) {
        super.markLoaded();
        return null;
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
                    System.out.println("\teffect <name> [args]   | select the effect with given name");
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
                    program().getEffectLoader().allEffects().values().forEach(effect -> System.out.printf("\t%-16s | %-32s | %-16s\r\n", effect.getName(), effect.getDescription(), effect.getAuthor()));
                    System.out.print("\r\n> ");
                }else if(cmd[0].equalsIgnoreCase("effect") && cmd.length >= 2){
                    String effectName = cmd[1];
                    String args = "";
                    if(cmd.length >= 3){
                        args = cmd[2];
                    }

                    Effect effect = program().getEffectLoader().allEffects().get(effectName);

                    if(effect == null){
                        System.out.println("Effect "+effectName+" does not exists");
                    }else{
                        System.out.print("> ");
                        program().setEffect(effect, args);
                    }

                }else if(cmd[0].equalsIgnoreCase("current")){
                    System.out.println("current Effect:");
                    System.out.println("\tname:        "+program().getEffect().getName());
                    System.out.println("\tdescription: "+program().getEffect().getDescription());
                    System.out.println("\tautor:       "+program().getEffect().getAuthor());
                    System.out.println("\tprefFps:     "+program().getEffect().getPreferedFPS());
                    System.out.print("> ");
                }else if(cmd[0].equalsIgnoreCase("reload")){
                    program().reload();
                    System.out.print("> ");
                }else if(cmd[0].equalsIgnoreCase("quit")){
                    program().exit();
                }

            }
        } catch (IOException e) {
            program().exit();
        }

    }

    @Override
    public String onStart(File root) {
        Thread inputThread = new Thread(this::inputLoop);
        inputThread.start();
        return null;
    }

    @Override
    public void onEffectChange(Effect effect) {
        System.out.print("\b\b");
        System.out.println("now playing effect " + effect.getName());
        System.out.print("> ");
    }
}
