package me.jingwang.log4j.mask;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class MaskingConverter {
    private static final List<Character> STOP_CHARACTERS = Arrays.asList('\'', '"', '@', '>');

    private static final Map<String, LogMasker> OPTIONS_TO_MASKER = initializeDefaultMaskers();

    private static final List<LogMasker> ALL_MASKERS = OPTIONS_TO_MASKER.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

    private List<LogMasker> maskers = new ArrayList<>();

    private List<SequentialLogMasker> sequentialMaskers = new ArrayList<>();

    public void init(List<String> options) {
        if (options != null) {
            for (String option : options) {
                if (StringUtils.startsWith(option, "custom")) {
                    maskers.addAll(buildCustomMaskersList(option));
                    sequentialMaskers.addAll(buildCustomSequentialMaskersList(option));
                } else if (option.equalsIgnoreCase("all")) {
                    maskers.addAll(ALL_MASKERS);
                } else {
                    LogMasker masker = getMaskerFromOptions(option);
                    maskers.add(masker);
                }
            }
        }

        if (maskers.isEmpty()) {
            maskers.addAll(ALL_MASKERS);
        }
    }

    public void setMaskers(List<LogMasker> maskers) {
        this.maskers.clear();
        this.maskers.addAll(maskers);
    }

    private LogMasker getMaskerFromOptions(String option) {
        String args = null;
        int idxOfArgsSeparator = StringUtils.indexOf(option, ':');
        if (idxOfArgsSeparator > 0) {
            args = StringUtils.substring(option, idxOfArgsSeparator + 1);
            option = StringUtils.substring(option, 0, idxOfArgsSeparator);
        }
        LogMasker masker = OPTIONS_TO_MASKER.get(option);
        if (masker == null) {
            throw new ExceptionInInitializerError("Invalid option provided: " + option);
        }
        if (args != null) {
            masker.initialize(args);
        }

        return masker;
    }

    private static List<LogMasker> buildCustomMaskersList(String params) {
        int idxOfArgsSeparator = StringUtils.indexOf(params, ':');
        if (idxOfArgsSeparator < 0) {
            return Collections.emptyList();
        }
        List<LogMasker> maskers = new ArrayList<>();

        String args = StringUtils.substring(params, idxOfArgsSeparator + 1);
        String[] packages = StringUtils.split(args, '|');
        for (String pack:packages) {
            Reflections reflections = new Reflections(pack);
            initializeCustomLogMaskers(maskers, reflections);
        }

        return maskers;
    }

    private static void initializeCustomLogMaskers(List<LogMasker> maskers, Reflections reflections) {
        Set<Class<? extends LogMasker>> allClasses = reflections.getSubTypesOf(LogMasker.class);
        for (Class<? extends LogMasker> clazz:allClasses) {
            try {
                Constructor<? extends LogMasker> maskerConstructor = clazz.getConstructor();
                LogMasker masker = maskerConstructor.newInstance();
                maskers.add(masker);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private static List<SequentialLogMasker> buildCustomSequentialMaskersList(String params) {
        int idxOfArgsSeparator = StringUtils.indexOf(params, ':');
        if (idxOfArgsSeparator < 0) {
            return Collections.emptyList();
        }
        List<SequentialLogMasker> maskers = new ArrayList<>();

        String args = StringUtils.substring(params, idxOfArgsSeparator + 1);
        String[] packages = StringUtils.split(args, '|');
        for (String pack:packages) {
            Reflections reflections = new Reflections(pack);
            initializeCustomSequentialLogMaskers(maskers, reflections);
        }

        return maskers;
    }

    private static void initializeCustomSequentialLogMaskers(List<SequentialLogMasker> maskers, Reflections reflections) {
        Set<Class<? extends SequentialLogMasker>> allClasses = reflections.getSubTypesOf(SequentialLogMasker.class);
        for (Class<? extends SequentialLogMasker> clazz:allClasses) {
            try {
                Constructor<? extends SequentialLogMasker> maskerConstructor = clazz.getConstructor();
                SequentialLogMasker masker = maskerConstructor.newInstance();
                maskers.add(masker);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public void mask(StringBuilder stringBuilder) {
        boolean maskedThisCharacter;
        int pos;
        int newPos;
        int length = stringBuilder.length();
        for (pos = 0; pos < length; pos++) {
            maskedThisCharacter = false;
            for (LogMasker masker : maskers) {
                newPos = masker.maskData(stringBuilder, '*', pos, length);
                maskedThisCharacter = newPos != pos;
                if (maskedThisCharacter) {
                    length = stringBuilder.length();
                    maskedThisCharacter = true;
                    break;
                }
            }
            if (!maskedThisCharacter) {
                while (pos < length && !(Character.isWhitespace(stringBuilder.charAt(pos)) || STOP_CHARACTERS.contains(stringBuilder.charAt(pos)))) {
                    pos++;
                }
            }
        }

        maskSequential(stringBuilder);
    }

    private void maskSequential(StringBuilder builder) {
        for (SequentialLogMasker masker: sequentialMaskers) {
            try {
                masker.mask(builder, '*');
            } catch (Exception e) {
                System.err.println("Error applying masker " + masker + ". Error: " + e.getMessage());
            }
        }
    }

    private static Map<String, LogMasker> initializeDefaultMaskers() {
        Map<String, LogMasker> maskers = new HashMap<>();
        maskers.put("pass", new PasswordMasker());

        return maskers;
    }
}
