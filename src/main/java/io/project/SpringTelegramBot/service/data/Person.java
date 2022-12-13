package io.project.SpringTelegramBot.service.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString

public class Person {
    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final int messagesNumber;

    public byte[] personsListInBytes() {
        return (this.getId() + ", "+
                this.getName() + ", " +
                this.getMessagesNumber() + "\n").getBytes();
    }
}


