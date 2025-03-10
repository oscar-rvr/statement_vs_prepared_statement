package org.example.model;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
public class User {
    private int id;
    @NonNull private String username;
    @NonNull private String password;

    // Constructor con solo username y password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor con todos los campos
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
