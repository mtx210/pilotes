package com.tui.pilotes.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderType {

    PILOTES_5("5 PILOTES"),
    PILOTES_10("10 PILOTES"),
    PILOTES_15("15 PILOTES");

    private final String name;
}