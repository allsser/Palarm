package com.example.palarm.utils;

import com.example.palarm.palarm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DebugUtils {

    private static final String[] SETUP_TASKS = {
            "com.example.palarm.utils.LeakCanaryTask",
//            "com.example.palarm.utils.CrasherTask"
    };

    /**
     * Set up any debug modules from the registered tasks. Should
     * be called inside the Application class's onCreate.
     *
     * @param palarm An instance of the current application class.
     */
    public static void setup(palarm palarm) {
        for (String task : SETUP_TASKS) {
            try {
                Constructor<SetupTask> constructor = (Constructor<SetupTask>) Class.forName(task).getConstructor();
                constructor.setAccessible(true);
                constructor.newInstance().setup(palarm);
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public interface SetupTask {
        void setup(palarm palarm);
    }

}
