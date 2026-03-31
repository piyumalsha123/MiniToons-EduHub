package lk.ijse.minitoonseduhub.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import lk.ijse.minitoonseduhub.dto.*;
import lk.ijse.minitoonseduhub.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/parent")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    private static final String SECRET_KEY = "mysecrty3424WEe32fmysecrty3424WEe32fmysecrty3424WEe32fmysecrty3424WEe32f";

    @GetMapping("/children")
    public ResponseEntity<?> getChildren(@RequestHeader("Authorization") String token) {
        try {
            String username = extractUsernameFromToken(token);
            List<ChildResponseDto> children = childService.getChildrenByParent(username);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "Unauthorized ❌", null));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> addChild(
            @RequestBody ChildCreateDto dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String username = extractUsernameFromToken(token);
            String result = childService.createChild(dto, username);
            return ResponseEntity.ok(new ApiResponse(200, "Child Added ✅", result));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @PutMapping("/update/{childId}")
    public ResponseEntity<ApiResponse> updateChild(
            @PathVariable Long childId,
            @RequestBody ChildUpdateDto dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String username = extractUsernameFromToken(token);
            childService.updateChild(childId, dto, username);
            return ResponseEntity.ok(new ApiResponse(200, "Child Updated ✅", null));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "Unauthorized ❌", null));
        }
    }

    @DeleteMapping("/delete/{childId}")
    public ResponseEntity<ApiResponse> deleteChild(
            @PathVariable Long childId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            extractUsernameFromToken(token); // validate token
            childService.deleteChild(childId);
            return ResponseEntity.ok(new ApiResponse(200, "Child Deleted ✅", null));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "Unauthorized ❌", null));
        }
    }

    private String extractUsernameFromToken(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        token = token.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();

        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token");
        }
    }
}