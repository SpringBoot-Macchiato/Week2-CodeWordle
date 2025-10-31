package com.crudzaso.codewordle.model;

public enum LetterStatus {
    CORRECT,    // Letter is correct and in right position
    PRESENT,    // Letter is in word but wrong position
    ABSENT      // Letter is not in word
}