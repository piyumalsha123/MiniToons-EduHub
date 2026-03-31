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

            return ResponseEntity.ok(new ApiResponse(
                    200,
                    "User logged in successfully",
                    userService.authenticate(authDTO)
            ));
        } catch (RuntimeException e) {

            return ResponseEntity.status(401).body(new ApiResponse(
                    401,
                    e.getMessage(),
                    null
            ));
        }
    }
}
