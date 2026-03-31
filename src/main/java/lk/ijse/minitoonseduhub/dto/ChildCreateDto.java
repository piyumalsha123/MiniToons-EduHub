package lk.ijse.minitoonseduhub.dto;

import lombok.Data;

@Data
public class ChildCreateDto {
    private String username;
    private String password;
    private String name;
    private int age;
    private String gender;
}