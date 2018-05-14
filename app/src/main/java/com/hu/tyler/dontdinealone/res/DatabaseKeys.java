package com.hu.tyler.dontdinealone.res;

public interface DatabaseKeys {
    public interface Profile {
        public static String DISPLAY_NAME = "display_name";
        public static String GENDER = "gender";
        public static String ANIMAL = "animal";
    }

    public interface Preference {
        public static String[] GROUP_SIZES = {"2", "3", "4"};
        public static String[] DINING_HALLS = { "NineTen", "CowellStevenson",
                "CrownMerill", "PorterKresge", "RachelCarsonOaks"};
    }
}
