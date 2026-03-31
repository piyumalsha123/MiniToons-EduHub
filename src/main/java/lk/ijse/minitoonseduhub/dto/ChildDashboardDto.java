package lk.ijse.minitoonseduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildDashboardDto {
    private String id;   // 👈 Dropdown එකේ value එකට ඕන වෙනවා
    private String name; // 👈 Dropdown එකේ display කරන්න ඕන වෙනවා
}