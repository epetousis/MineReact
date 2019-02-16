package com.evanpetousis.minereact;

import java.util.LinkedHashMap;

enum Reaction {
    HAPPY,
    SAD,
    ANGRY,
    HEARTEYES,
    HEART,
    COMMUNISM,
    BRUH,
    DENKO,
    YAY,
    SHRUG,
}

class ReactableMessage {
    String message;
    private LinkedHashMap<Reaction, Integer> reacts;

    ReactableMessage(String message) {
        this.message = message;
        this.reacts = new LinkedHashMap<>();
    }

    Integer getReacts(Reaction react) {
        return reacts.getOrDefault(react, 0);
    }

    String getReactIcon(Reaction react) {
        switch (react) {
            case HAPPY:
                return "☺";
            case SAD:
                return "☹";
            case ANGRY:
                return "(ò_óˇ)";
            case HEARTEYES:
                return "(❤▽❤)";
            case HEART:
                return "❤";
            case COMMUNISM:
                return "☭";
            case BRUH:
                return "(￣ｰ￣)";
            case DENKO:
                return "(´・ω・`)";
            case YAY:
                return "ヽ(゜∇゜)ノ";
            case SHRUG:
                return "¯\\_(ツ)_/¯";
            default:
                return "";
        }
    }

    void addReact(Reaction react) {
        int newReactCount = reacts.getOrDefault(react, 0) + 1;
        reacts.put(react, newReactCount);
    }

    void removeReact(Reaction react) {
        int newReactCount = reacts.getOrDefault(react, 0) - 1;
        reacts.put(react, newReactCount);
    }
}
