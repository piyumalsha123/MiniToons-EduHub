package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.ApiResponse;
import lk.ijse.minitoonseduhub.dto.AuthDto;
import lk.ijse.minitoonseduhub.dto.RegisterDto;
import lk.ijse.minitoonseduhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody RegisterDto registerDTO){
        return ResponseEntity.ok(new ApiResponse(
                200,
                "User registered successfully",
                userService.saveUser(registerDTO)
        ));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody AuthDto authDTO){
        try {
            // 1. කලින් වගේම login එක සාර්ථක නම් 200 එක්ක Token එක යනවා
            return ResponseEntity.ok(new ApiResponse(
                    200,
                    "User logged in successfully",
                    userService.authenticate(authDTO)
            ));
        } catch (RuntimeException e) {
            // 2. ⚠️ UserService එක ඇතුළේ මොකක් හරි Exception එකක් (වැරැද්දක්) විසි වුණොත් මේකට අහුවෙනවා
            // එතකොට අපි 401 (Unauthorized) code එකත් එක්ක ඒ වැරැද්ද මොකක්ද කියලා යවනවා
            return ResponseEntity.status(401).body(new ApiResponse(
                    401,
                    e.getMessage(), // 👈 මෙතනින් තමයි "Invalid Password" වගේ එක එන්නේ
                    null
            ));
        }
    }
}
