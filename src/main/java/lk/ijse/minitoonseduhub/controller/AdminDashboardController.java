package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.entity.Role;
import lk.ijse.minitoonseduhub.repository.ChildProfileRepository;
import lk.ijse.minitoonseduhub.repository.LessonRepository;
import lk.ijse.minitoonseduhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminDashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildProfileRepository childProfileRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("parents", userRepository.countByRole(Role.PARENT));
        stats.put("children", childProfileRepository.count());
        stats.put("lessons", lessonRepository.count());
        return stats;
    }
}