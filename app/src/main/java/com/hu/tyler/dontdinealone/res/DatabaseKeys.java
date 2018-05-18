package com.hu.tyler.dontdinealone.res;

public interface DatabaseKeys {
    String TIMESTAMP = "timestamp";

    interface Profile {
        String DISPLAY_NAME = "display_name";
        String GENDER = "gender";
        String ANIMAL = "animal";
    }

    interface Preference {
        String[] GROUP_SIZES = {"2", "3", "4"};
        String[] DINING_HALLS = { "NineTen", "CowellStevenson",
                "CrownMerill", "PorterKresge", "RachelCarsonOaks"};
    }
}
