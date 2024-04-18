package com.example.chat.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class TypingNotification {
    private int fromId;
    private int toId;
    private boolean typing;
}
