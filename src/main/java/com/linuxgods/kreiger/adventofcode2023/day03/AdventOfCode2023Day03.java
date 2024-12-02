package com.linuxgods.kreiger.adventofcode2023.day03;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.linuxgods.kreiger.adventofcode2023.day01.AdventOfCode2023Day01.lines;

public class AdventOfCode2023Day03 {

    private final static Pattern NUMBER = Pattern.compile("(\\d+)");
    private final static Pattern STAR = Pattern.compile("\\*");

    public static void main(String[] args) {
        List<String> lines = lines(AdventOfCode2023Day03.class.getResourceAsStream("input")).toList();
        int sum = 0;
        List<List<NumberSpan>> spans = lines.stream().map(line -> {
            List<NumberSpan> numberSpans = new ArrayList<>();
            Matcher matcher = NUMBER.matcher(line);
            while (matcher.find()) {
                numberSpans.add(new NumberSpan(matcher.start(), matcher.end(), Integer.parseInt(matcher.group())));
            }
            return numberSpans;
        }).toList();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            Matcher star = STAR.matcher(line);
            while (star.find()) {
                List<Integer> partNumbers = new ArrayList<>();
                if (y > 0) {
                    List<NumberSpan> spansAbove = spans.get(y - 1);
                    for (NumberSpan span : spansAbove) {
                        if (span.start() <= star.end() && span.end() >= star.start()) {
                            partNumbers.add(span.number());
                        }
                    }
                }
                List<NumberSpan> lineSpans = spans.get(y);
                for (NumberSpan span : lineSpans) {
                    if (span.start() == star.end() || span.end() == star.start()) {
                        partNumbers.add(span.number());
                    }
                }
                if (y < lines.size() - 1) {
                    List<NumberSpan> spansBelow = spans.get(y + 1);
                    for (NumberSpan span : spansBelow) {
                        if (span.start() <= star.end() && span.end() >= star.start()) {
                            partNumbers.add(span.number());
                        }
                    }
                }
                if (partNumbers.size() == 2) {
                    sum += partNumbers.get(0) * partNumbers.get(1);
                }
            }
        }
        System.out.println(sum);
    }

    private static boolean hasSymbol(String substring) {
        for (int i = 0; i < substring.length(); i++) {
            char c = substring.charAt(i);
            if (isSymbol(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }

    private record NumberSpan(int start, int end, int number) {
    }
}
