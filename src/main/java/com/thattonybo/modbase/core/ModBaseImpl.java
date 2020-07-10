package com.thattonybo.modbase.core;

import com.thattonybo.modbase.errors.UnknownClassException;
import com.thattonybo.modbase.metadata.ModConfig;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Implementation on how to load the Mod
 */
public class ModBaseImpl {
    /**
     * Gson instance to convert from JSON to Java
     */
    private final Gson gson = new Gson();

    /**
     * Function to get the Mod's class instance
     */
    public Class<?> getModClass() throws UnknownClassException {
        InputStream stream = this.getClass().getResourceAsStream("/mod.json");
        String content = new BufferedReader(new InputStreamReader(stream))
                .lines()
                .collect(Collectors.joining("\n"));

        ModConfig config = this.gson.fromJson(content, ModConfig.class);
        Class<?> mainClass;
        try {
            mainClass = Class.forName(config.className);
        } catch (ClassNotFoundException e) {
            throw new UnknownClassException(config.className);
        }

        return mainClass;
    }

    /**
     * Loads the mod
     * @throws com.thattonybo.modbase.errors.UnknownClassException Thrown if the "mod.json" file includes
     * a non-class we can access
     */
    public void load() throws Exception {
        Class<?> mainClass = getModClass();
        Method onEnable = mainClass.getDeclaredMethod("onEnable");
        Object instance = mainClass.newInstance();

        onEnable.invoke(instance);

        addCycleHook();
    }

    /**
     * Adds a lifecycle hook when the application is closing (invokes "onDisable")
     */
    private void addCycleHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Class<?> mainClass = getModClass();
                Method onDisable = mainClass.getDeclaredMethod("onDisable");
                Object instance = mainClass.newInstance();

                onDisable.invoke(instance);
            } catch (UnknownClassException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }));
    }
}