package io.project.SpringTelegramBot.database;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {}
