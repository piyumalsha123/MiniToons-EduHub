package lk.ijse.minitoonseduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String access_token;
    private Long id;       // 👈 🚨 මේක අලුතින් එකතු කරන්න!
    private String name;
    private String gender;
}