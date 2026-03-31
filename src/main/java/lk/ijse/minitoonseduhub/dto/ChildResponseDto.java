package lk.ijse.minitoonseduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChildResponseDto {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String username;
}