package io.project.SpringTelegramBot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Data
@Entity(name = "tg_data") //клас прив'язати до реальної таблиці
public class User {

    @Id
    private long id;
    private String name;
    private int msg_numb;
}
