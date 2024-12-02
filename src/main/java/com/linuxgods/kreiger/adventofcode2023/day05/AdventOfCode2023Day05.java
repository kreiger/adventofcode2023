package com.linuxgods.kreiger.adventofcode2023.day05;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.linuxgods.kreiger.adventofcode2023.day01.AdventOfCode2023Day01.lines;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toMap;

public class AdventOfCode2023Day05 {

    private final static Pattern SEEDS = Pattern.compile("^seeds: (\\d.*\\d)");
    private final static Pattern SEED = Pattern.compile("(\\d+) (\\d+)");
    private final static Pattern MAP = Pattern.compile("^(\\w+)-to-(\\w+) map:");
    private final static Pattern LONGS = Pattern.compile("(\\d+) (\\d+) (\\d+)");

    public static void main(String[] args) {
        List<String> lines = lines(AdventOfCode2023Day05.class.getResourceAsStream("input")).collect(toList());
        String seedsLine = lines.remove(0);
        Matcher seedsMatcher = SEEDS.matcher(seedsLine);
        seedsMatcher.find();
        String seedsString = seedsMatcher.group(1);
        Matcher seedMatcher = SEED.matcher(seedsString);
        Set<Long> seeds = new HashSet<>();
        while (seedMatcher.find()) {
            long start = Long.parseLong(seedMatcher.group(1));
            long length = Long.parseLong(seedMatcher.group(2));
            for (long seed = start; seed < seed + length; seed++) {
                seeds.add(seed);
            }
        }
        Map<String, Destination> maps = new HashMap<>();
        Destination current = null;
        for (String line : lines) {
            Matcher mapMatcher = MAP.matcher(line);
            if (mapMatcher.find()) {
                maps.put(mapMatcher.group(1), current = new Destination(mapMatcher.group(2), new TreeMap<>()));
                continue;
            }
            Matcher longs = LONGS.matcher(line);
            if (longs.find()) {
                long destination = Long.parseLong(longs.group(1));
                long source = Long.parseLong(longs.group(2));
                long length = Long.parseLong(longs.group(3));
                current.map.put(source, new Range(source, destination, length));
            }
        }

        NavigableMap<Long, Range> merged = mergeMaps(maps);
        seedsAndMaps(seeds, maps);
    }

    private static NavigableMap<Long, Range> mergeMaps(Map<String, Destination> maps) {
        NavigableMap<Long, Range> merged = new TreeMap<>();
        for (Destination d = maps.get("seed"); d != null; d = maps.get(d.id())) {
            merged.putAll(d.map());
        }
        return merged;
    }

    private static void seedsAndMaps(Set<Long> seeds, Map<String, Destination> maps) {
        long minValue = Long.MAX_VALUE;
        for (Long value : seeds) {
            for (Destination d = maps.get("seed"); d != null; d = maps.get(d.id())) {
                System.out.println(d.id + " "+value);
                value = d.get(value);
            }
            if (value < minValue) minValue = value;
        }
        System.out.println(minValue);
    }

    private static Stream<Long> parseLongs(String string) {
        return Stream.of(string.split("\\s+"))
                .map(Long::parseLong);
    }

    private record Destination(String id, NavigableMap<Long, Range> map) {
        long get(long key) {
            Map.Entry<Long, Range> entry = map.floorEntry(key);
            if (entry == null) return key;
            return entry.getValue().get(key);
        }
    }

    private record Range(long source, long destination, long length) {
        public long get(long key) {
            if (key < source || key >= source + length) return key;
            return key - source + destination;
        }
    }
}
