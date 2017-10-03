package de.diavololoop.chloroplast;

import de.diavololoop.chloroplast.config.Config;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.modules.Module;
import de.diavololoop.chloroplast.modules.cmdline.ModuleCommandline;
import de.diavololoop.chloroplast.modules.gui.ModuleTestGui;
import de.diavololoop.chloroplast.modules.httpserver.ModuleHTTPServer;
import de.diavololoop.chloroplast.modules.websocket.ModuleWebSocket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws IOException {

        if(args.length == 1 && args[0].equalsIgnoreCase("-h")){
            printUsage();
            return;
        }

        DiscoMaker program = new DiscoMaker();
        Config config = null;
        boolean redirectHosts = false;
        File root = new File(".");

        //prepare all modules
        List<Module> modules = new ArrayList<>();
        modules.add(new ModuleCommandline(program));
        modules.add(new ModuleHTTPServer(program));
        modules.add(new ModuleWebSocket(program));
        modules.add(new ModuleTestGui(program));

        for(int i = 0; i < args.length; ++i) {

            //some general settings, which can not be outsourced in modules
            if (args[i].equalsIgnoreCase("-c") && args.length > i+1) {
                config = new Config(new File(args[i+1]));
                ++i;
                continue;
            } else if (args[i].equalsIgnoreCase("-t")) {
                redirectHosts = true;
            }

            //checks for selecting module and args length
            String key = args[i];
            Optional<Module> module = modules.stream().filter(m -> m.getKey().equalsIgnoreCase(key)).findFirst();

            if(!module.isPresent()){
                System.out.println("not known parameter " + key);
                return;
            }

            if(args.length <= i + module.get().getParameterCount()) {
                System.out.println("syntax error for key: " + key);
                printUsage();
                return;
            }

            //preparing module specific arguments and init the module with it
            String[] moduleArguments = Arrays.copyOfRange(args, i + 1, i + 1 + module.get().getParameterCount());
            String exception = module.get().init(moduleArguments);
            if(exception != null){
                System.err.println(exception);
                return;
            }

            i += module.get().getParameterCount();
        }

        if(config == null) {
            System.out.println("cant start without a led-configuration file");
            System.out.println("use the -c <file> parameter");
            return;
        }
        if(redirectHosts) {
            config.redirectAllToLocalhost();
        }

        Consumer<Effect> effectChanged = (effect) -> modules.stream().filter(m -> m.isLoaded()).forEach(m -> m.onEffectChange(effect));
        Runnable onQuit = () -> modules.stream().filter(m -> m.isLoaded()).forEach(m -> m.onQuit());
        Runnable onReload = () -> modules.stream().filter(m -> m.isLoaded()).forEach(m -> m.onReaload());

        program.init(root, config, effectChanged, onQuit, onReload);
        modules.stream().filter(m -> m.isLoaded()).map(m -> m.onStart(root)).filter(s -> s != null).forEach(s -> {
                System.err.println(s);
                return;
        });

    }

    public static void printUsage(){
        System.out.println("usage <args>");
        System.out.println("\t[-h]          \tprints this help");
        System.out.println("\t[-t]          \tsends the data to a GUI instead of address");
        System.out.println("\t[-http] <port>\tstarts a webinterface on given port");
        System.out.println("\t[-ws]   <port>\tstarts a websocket interface on given port");
        System.out.println("\t[-i]          \tstarts listening on stdin for commands");
        System.out.println("\t -c     <file>\tthe led-configuration file");
    }

}
