package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.ChildDashboardDto;
import lk.ijse.minitoonseduhub.dto.ChildHistoryDto;
import lk.ijse.minitoonseduhub.entity.ChildProfile;
import lk.ijse.minitoonseduhub.service.ParentChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/parent-child")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342") // Frontend Origin එක
public class ParentChildController {

    private final ParentChildService parentChildService;

    // 👨‍👩‍👦 1. Parent ට අයිති ළමයි ලිස්ට් එක ගන්න Method එක
    @GetMapping("/my-children")
    public ResponseEntity<List<ChildDashboardDto>> getMyChildren(Principal principal) {
        String parentUsername = principal.getName();

        List<ChildProfile> myChildren = parentChildService.getChildrenByParentUsername(parentUsername);

        // Entity list එක Dashboard DTO list එකකට Map කරනවා
        List<ChildDashboardDto> dtoList = myChildren.stream()
                .map(child -> new ChildDashboardDto(
                        String.valueOf(child.getId()),
                        child.getName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/insights/{childId}")
    public ResponseEntity<?> getInsights(@PathVariable Long childId) {
        return ResponseEntity.ok(parentChildService.getInsights(childId));
    }

    @GetMapping("/history/{childId}")
    public ResponseEntity<List<ChildHistoryDto>> getChildHistory(@PathVariable Long childId) {
        return ResponseEntity.ok(parentChildService.getChildHistory(childId));
    }
}