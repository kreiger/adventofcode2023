package com.linuxgods.kreiger.adventofcode2023.day02;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.linuxgods.kreiger.adventofcode2023.day01.AdventOfCode2023Day01.lines;

public class AdventOfCode2023Day02 {
    private final static Pattern GAME_PATTERN = Pattern.compile("^Game (\\d+): (.*)$");

    private static final Map<String, Integer> MAX_POSSIBLE = Map.of("red", 12, "green", 13, "blue", 14);

    public static void main(String[] args) {
        int sum = lines(AdventOfCode2023Day02.class.getResourceAsStream("input"))
                .mapToInt(line -> {
                    Matcher matcher = GAME_PATTERN.matcher(line);
                    matcher.find();
                    int id = Integer.parseInt(matcher.group(1));
                    String[] sets = matcher.group(2).split("; ");
                    Map<String, Integer> min = new HashMap<>(Map.of("red", 0, "green", 0, "blue", 0));
                    for (String set : sets) {
                        String[] cubeCounts = set.split(", ");
                        for (String cubeCount : cubeCounts) {
                            String[] countAndColor = cubeCount.split(" ");
                            int count = Integer.parseInt(countAndColor[0]);
                            String color = countAndColor[1];
                            min.computeIfPresent(color, (k, v) -> Math.max(v, count));
                        }
                    }
                    int power = min.get("red") * min.get("green") * min.get("blue");
                    return power;

                }).sum();
        System.out.println(sum );
    }
}
