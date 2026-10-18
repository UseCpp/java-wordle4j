package ru.yandex.practicum.wordle;

import ru.yandex.practicum.WordleDictionary;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WordleGame {

    public record ProcessedInput(String input, String mask) {
        @Override
        public String toString() {
            return input + System.lineSeparator() + mask;
        }
    }

    private String answer;

    private Set<Character> answerCharacters;

    private Set<String> userInputs;

    private Set<Character> notValidChars;

    private Character[] userMask;

    private int steps;

    private final WordleDictionary dictionary;

    private final Random random;

    public WordleGame(WordleDictionary dictionary, Random random) {
        this.dictionary = dictionary;
        this.random = random;
    }

    public void initSession() {
        answer = dictionary.get(random.nextInt(dictionary.size()));
        answerCharacters = getCharacterSet(answer);
        steps = 6;
        userInputs = new HashSet<>();
        userMask = new Character[answer.length()];
        Arrays.fill(userMask, '-');
        notValidChars = new HashSet<>();
    }

    public ProcessedInput processInput(String userInput) throws WordleGameException {
        if (!canPlay()) {
            throw new WordleUnexpectedInput("Неожиданный ввод в законченную игру. Перезапустите сессию.");
        }

        userInput = WordleDictionary.normalize(userInput);

        if (userInput.isEmpty()) {
            userInput = getAssistantWord();
        }

        if (dictionary.contains(userInput)) {
            steps--;
        } else {
            throw new WordNotFoundInDictionary(userInput);
        }
        userInputs.add(userInput);

        if (userInput.equals(answer)) {
            steps = 0;
        }

        return new ProcessedInput(userInput, formMask(userInput));
    }

    private String getAssistantWord() {
        var potentialWords = dictionary.stream()
                .filter(Predicate.not(userInputs::contains))
                .filter(str -> Collections.disjoint(getCharacterSet(str), notValidChars))
                .filter(this::validByMask)
                .toList();

        for (int i = 0; i < userMask.length; i++) {
            if (userMask[i] == '-') {
                for (var word : potentialWords) {
                    if (word.charAt(i) == answer.charAt(i)) {
                        return word;
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    private boolean validByMask(String word) {
        for (int i = 0; i < userMask.length; i++) {
            var smb = userMask[i];
            if (smb == answer.charAt(i) && word.charAt(i) != smb) {
                return false;
            }
        }
        return true;
    }

    private Set<Character> getCharacterSet(String word) {
        return word.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }

    private String formMask(String userInput) {
        StringBuilder maskBuilder = new StringBuilder();
        for (int i = 0; i < userInput.length(); i++) {
            char smb = userInput.charAt(i);
            if (smb == answer.charAt(i)) {
                maskBuilder.append("+");
                userMask[i] = smb;
            } else {
                if (answerCharacters.contains(smb)) {
                    maskBuilder.append("^");
                } else {
                    maskBuilder.append("-");
                    notValidChars.add(smb);
                }
            }
        }
        return maskBuilder.toString();
    }


    public int getSteps() {
        return steps;
    }

    public boolean canPlay() {
        return steps > 0;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean win() {
        return userInputs.contains(answer);
    }
}
