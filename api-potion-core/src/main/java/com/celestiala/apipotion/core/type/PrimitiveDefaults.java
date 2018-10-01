package com.celestiala.apipotion.core.type;

import java.util.HashMap;

public class PrimitiveDefaults {

    private static HashMap<Class,Object> defaults=new HashMap<>();

    static {
        defaults.put(boolean.class,false);
        defaults.put(byte.class,0);
        defaults.put(short.class,0);
        defaults.put(int.class,0);
        defaults.put(long.class,0L);
        defaults.put(float.class,0);
        defaults.put(double.class,0);
    }

    public static Object getDefault(Class clazz){
        return defaults.get(clazz);
    }
}
