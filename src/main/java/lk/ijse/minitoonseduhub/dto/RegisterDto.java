package lk.ijse.minitoonseduhub.dto;

import lombok.Data;
@Data
public class RegisterDto {
    private String username;
    private String password;
    private String role;

    private String name;
    private int age;
    private String gender;
}