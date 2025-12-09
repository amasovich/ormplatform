package ru.mifi.ormplatform.domain.enums;

/**
 * Тип вопроса в квизе (single/multiple/text).
 * В БД хранится как строка в колонке type.
 */
public enum QuestionType {
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    TEXT
}
