package de.diavololoop.chloroplast.effect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by gast2 on 26.09.17.
 */
public class EffectWrapperClass implements Effect{

    private String name;
    private String author;
    private String description;
    private int preferedFPS;

    private Method initMethod;
    private Method updateMethod;

    private Object instance;

    public EffectWrapperClass(Class<?> cls) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.instance = cls.newInstance();

        Method[] methods = cls.getDeclaredMethods();
        for(Method m: methods){

            if(m.getName().equals("getName")){
                Object result = m.invoke(instance);
                if(result instanceof String){
                    name = (String)result;
                }
            }else if(m.getName().equals("getAuthor")){
                Object result = m.invoke(instance);
                if(result instanceof String){
                    author = (String)result;
                }
            }else if(m.getName().equals("getDescription")){
                Object result = m.invoke(instance);
                if(result instanceof String){
                    description = (String)result;
                }
            }else if(m.getName().equals("getPreferedFPS")){
                Object result = m.invoke(instance);
                if(result instanceof Integer){
                    preferedFPS = (int)result;
                }else{
                    System.err.println("Effect getPreferedFPS not return int: "+cls.getName());
                }
            }else if(m.getName().equals("init")){
                Class<?>[] types = m.getParameterTypes();
                if(types.length == 2 && types[0] == int.class && types[1] == String.class){
                    initMethod = m;
                }else{
                    throw new IllegalArgumentException("init method must match init(int nleds, String args)");
                }
            }else if(m.getName().equals("update")){
                Class<?>[] types = m.getParameterTypes();
                if(types.length == 3 && types[0] == long.class && types[1] == int.class && types[2].isArray() && types[2].getComponentType() == byte.class){
                    updateMethod = m;
                }else{
                    throw new IllegalArgumentException("update method must match update(long time, int step, byte[] data)");
                }
            }

        }

        System.out.println(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPreferedFPS() {
        return preferedFPS;
    }

    @Override
    public void init(int nleds, String args) {
        try {
            initMethod.invoke(instance, nleds, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(long time, int step, byte[] data) {
        try {
            updateMethod.invoke(instance, time, step, data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void kill() {
    }

    @Override
    public String toString(){
        return "effect{name="+getName()+", author="+getAuthor()+", description="+getDescription()+", fps="+getPreferedFPS()+", class="+this.getClass().getName()+"}";
    }
}
