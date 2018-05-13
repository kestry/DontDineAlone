package com.hu.tyler.dontdinealone.models;

import com.hu.tyler.dontdinealone.R;

public interface DatabaseKeys {
    public interface Profile {
        public static String DISPLAY_NAME = "display_name";
        public static String GENDER = "gender";
        public static String ANIMAL = "animal";
    }

    public interface Preference {
        public static String[] groupSizes = {"2", "3", "4"};
        public static String[] diningHalls = { "NineTen", "CowellStevenson",
                "CrownMerill", "PorterKresge", "RachelCarsonOaks"};
    }
}
