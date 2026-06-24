package com.chitkara.bfhl.service;

import com.chitkara.bfhl.config.UserProperties;
import com.chitkara.bfhl.dto.BfhlRequest;
import com.chitkara.bfhl.dto.BfhlResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BfhlServiceImpl implements BfhlService {

    private static final DateTimeFormatter DOB_INPUT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DOB_OUTPUT = DateTimeFormatter.ofPattern("ddMMyyyy");

    private final UserProperties userProperties;

    public BfhlServiceImpl(UserProperties userProperties) {
        this.userProperties = userProperties;
    }

    @Override
    public BfhlResponse process(BfhlRequest request) {
        List<String> data = request.getData() != null ? request.getData() : List.of();

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        List<Character> alphaChars = new ArrayList<>();
        long sum = 0;

        for (String item : data) {
            if (item == null || item.isEmpty()) {
                specialCharacters.add(item != null ? item : "");
                continue;
            }

            if (isNumeric(item)) {
                long value = Long.parseLong(item);
                sum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlphabetic(item)) {
                alphabets.add(item.toUpperCase());
                for (char c : item.toCharArray()) {
                    if (Character.isLetter(c)) {
                        alphaChars.add(c);
                    }
                }
            } else {
                specialCharacters.add(item);
                for (char c : item.toCharArray()) {
                    if (Character.isLetter(c)) {
                        alphaChars.add(c);
                    }
                }
            }
        }

        BfhlResponse response = new BfhlResponse();
        response.setSuccess(true);
        response.setUserId(buildUserId());
        response.setEmail(userProperties.getEmail());
        response.setRollNumber(userProperties.getRollNumber());
        response.setOddNumbers(oddNumbers);
        response.setEvenNumbers(evenNumbers);
        response.setAlphabets(alphabets);
        response.setSpecialCharacters(specialCharacters);
        response.setSum(String.valueOf(sum));
        response.setConcatString(buildConcatString(alphaChars));
        return response;
    }

    private String buildUserId() {
        String namePart = userProperties.getFullName()
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "_");
        LocalDate dob = LocalDate.parse(userProperties.getDateOfBirth(), DOB_INPUT);
        return namePart + "_" + dob.format(DOB_OUTPUT);
    }

    private boolean isNumeric(String value) {
        if (value.charAt(0) == '-') {
            return value.length() > 1 && value.substring(1).chars().allMatch(Character::isDigit);
        }
        return value.chars().allMatch(Character::isDigit);
    }

    private boolean isAlphabetic(String value) {
        return value.chars().allMatch(Character::isLetter);
    }

    private String buildConcatString(List<Character> alphaChars) {
        List<Character> reversed = new ArrayList<>(alphaChars);
        Collections.reverse(reversed);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.size(); i++) {
            char c = reversed.get(i);
            result.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return result.toString();
    }
}
