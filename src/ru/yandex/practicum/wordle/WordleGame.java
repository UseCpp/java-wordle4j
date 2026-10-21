package ru.yandex.practicum.wordle;

import ru.yandex.practicum.WordleDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class WordleGame {

    public static final int MAX_STEPS = 6;

    public static final char MASK_EXACT = '+';
    public static final char MASK_PRESENT = '^';
    public static final char MASK_ABSENT = '-';

    public enum State {
        NOT_STARTED,
        IN_PROGRESS,
        WIN, LOSE
    }

    public record ProcessedInput(String input, String mask) {
        @Override
        public String toString() {
            return input + System.lineSeparator() + mask;
        }
    }

    private final WordleDictionary dictionary;
    private final Random random;

    private State state = State.NOT_STARTED;
    private String answer;
    private int steps;
    private Set<String> userInputs;
    private WordleUserKnownInformation knownInformation;
    private List<String> candidates;

    public WordleGame(WordleDictionary dictionary, Random random) {
        this.dictionary = dictionary;
        this.random = random;
    }

    public void initSession() {
        answer = dictionary.get(random.nextInt(dictionary.size()));
        steps = MAX_STEPS;
        userInputs = new LinkedHashSet<>();
        knownInformation = new WordleUserKnownInformation(WordleDictionary.WORD_LENGTH);
        candidates = dictionary.stream().collect(Collectors.toCollection(ArrayList::new));
        state = State.IN_PROGRESS;
    }

    public ProcessedInput processInput(String word) throws WordleGameException {
        if (state != State.IN_PROGRESS) {
            throw new WordleUnexpectedInput(state == State.NOT_STARTED
                    ? "Игра ещё не начата. Ввод невозможен."
                    : "Игра уже завершена. Перезапустите сессию.");
        }

        if (word.isEmpty()) {
            word = candidates.get(random.nextInt(candidates.size()));
        } else {
            validate(word);
        }

        String mask = formMask(word);
        steps--;
        userInputs.add(word);
        knownInformation.analyze(word, mask);
        candidates.removeIf(candidate -> userInputs.contains(candidate) || !knownInformation.matches(candidate));

        if (word.equals(answer)) {
            state = State.WIN;
        } else if (steps == 0) {
            state = State.LOSE;
        }
        return new ProcessedInput(word, mask);
    }

    private void validate(String word) throws WordleGameException {
        if (word.length() != WordleDictionary.WORD_LENGTH) {
            throw new WordInvalidFormat("Слово должно состоять из " + WordleDictionary.WORD_LENGTH + " букв");
        }
        for (int i = 0; i < word.length(); i++) {
            char smb = word.charAt(i);
            if (smb < 'а' || smb > 'я') {
                throw new WordInvalidFormat("Слово должно состоять только из русских букв");
            }
        }
        if (!word.equals(answer) && !dictionary.contains(word)) {
            throw new WordNotFoundInDictionary("Слова \"" + word + "\" нет в словаре");
        }
    }

    private String formMask(String word) {
        char[] mask = new char[word.length()];
        Map<Character, Integer> unmatched = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == answer.charAt(i)) {
                mask[i] = MASK_EXACT;
            } else {
                unmatched.merge(answer.charAt(i), 1, Integer::sum);
            }
        }
        for (int i = 0; i < word.length(); i++) {
            if (mask[i] != MASK_EXACT) {
                char smb = word.charAt(i);
                int left = unmatched.getOrDefault(smb, 0);
                if (left > 0) {
                    mask[i] = MASK_PRESENT;
                    unmatched.put(smb, left - 1);
                } else {
                    mask[i] = MASK_ABSENT;
                }
            }
        }
        return new String(mask);
    }

    public int getSteps() {
        return steps;
    }

    public boolean canPlay() {
        return state == State.IN_PROGRESS;
    }

    public boolean win() {
        return state == State.WIN;
    }

    public String getAnswer() {
        return answer;
    }
}
