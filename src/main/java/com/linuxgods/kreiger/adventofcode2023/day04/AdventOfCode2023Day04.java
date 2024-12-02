package com.linuxgods.kreiger.adventofcode2023.day04;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static com.linuxgods.kreiger.adventofcode2023.day01.AdventOfCode2023Day01.lines;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class AdventOfCode2023Day04 {

    private final static Pattern CARD = Pattern.compile("^Card\\s+(\\d+): +(\\d.*?\\d) \\| +(\\d.*\\d)");

    public static void main(String[] args) {
        List<Card> cards = lines(AdventOfCode2023Day04.class.getResourceAsStream("input"))
                .map(line -> {
                    Matcher matcher = CARD.matcher(line);
                    matcher.find();
                    Set<Integer> numbersYouHave = parseInts(matcher.group(3));
                    numbersYouHave.retainAll(parseInts(matcher.group(2)));
                    return new Card(Integer.parseInt(matcher.group(1)), numbersYouHave.size());
                })
                .toList();
        Map<Integer, Integer> cardCounts = cards.stream()
                .collect(toMap(Card::id, c1 -> 1, Integer::sum, LinkedHashMap::new));
        for (Card card : cards) {
            for (int id = card.id()+1; id <= card.id()+card.won(); id++) {
                cardCounts.computeIfPresent(id, (key, won) -> won + cardCounts.get(card.id));
            }
        }
        int sum = cardCounts.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);
    }

    private static Set<Integer> parseInts(String string) {
        return Stream.of(string.split("\\s+"))
                .map(Integer::parseInt).collect(toSet());
    }

    private record Card(int id, int won) {
    }
}
