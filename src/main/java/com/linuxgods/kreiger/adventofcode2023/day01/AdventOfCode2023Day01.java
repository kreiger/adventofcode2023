package com.linuxgods.kreiger.adventofcode2023.day01;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AdventOfCode2023Day01 {
    static List<String> DIGITS = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    public static final String DIGITS_REGEX = String.join("|", DIGITS);
    private final static Pattern PATTERN = Pattern.compile("\\d|"+ DIGITS_REGEX);
    private final static Pattern REVERSE_PATTERN = Pattern.compile("\\d|"+reverse(DIGITS_REGEX));

    public static void main(String[] args) {
        Stream<String> lines = lines(AdventOfCode2023Day01.class.getResourceAsStream("input"));
        int sum = lines.mapToInt(line -> {
            Matcher matcher = PATTERN.matcher(line);
            matcher.find();
            int firstDigit = getDigit(matcher.group());
            Matcher reverseMatcher = REVERSE_PATTERN.matcher(reverse(line));
            reverseMatcher.find();
            int lastDigit = getDigit(reverse(reverseMatcher.group()));
            int parsed = firstDigit * 10 + lastDigit;
            System.out.println(line+ " "+firstDigit+" "+lastDigit+" "+parsed);
            return parsed;
        }).sum();

        System.out.println(sum);
    }

    public static Stream<String> lines(InputStream input) {
        Scanner scanner = new Scanner(input).useDelimiter("\\n");
        return scanner.tokens();
    }

    private static String reverse(String pattern) {
        return new StringBuilder(pattern).reverse().toString();
    }

    private static int getDigit(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return DIGITS.indexOf(string);
        }
    }
}