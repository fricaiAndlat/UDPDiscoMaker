package de.diavololoop.chloroplast.io;

import de.diavololoop.chloroplast.DiscoMaker;

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
    }

    private void inputLoop() {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

            while(!Thread.currentThread().isInterrupted()){
                String cmd[] = reader.readLine().split(" ");

                if(cmd[0].equalsIgnoreCase("help")){
                    System.out.println("commands:");
                    System.out.println("\thelp                   | prints this help");
                    System.out.println("\tlist                   | lists all available effects");
                    System.out.println("\teffect name [args]     | select the effect with given name ");
                    System.out.println("\tcurrent                | displays current effect and state");
                    System.out.println("\treload                 | reload the effects directory");
                    System.out.println("\tquit                   | exits the program");

                }

            }
        } catch (IOException e) {
            discoMaker.exit();
        }

    }


}
