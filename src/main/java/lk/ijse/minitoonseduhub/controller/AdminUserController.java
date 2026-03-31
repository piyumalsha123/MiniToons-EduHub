package lk.ijse.minitoonseduhub.controller;

import jakarta.transaction.Transactional;
import lk.ijse.minitoonseduhub.dto.UserDto;
import lk.ijse.minitoonseduhub.entity.User;
import lk.ijse.minitoonseduhub.repository.UserRepository;
import lk.ijse.minitoonseduhub.repository.ChildProfileRepository;
import lk.ijse.minitoonseduhub.repository.ParentChildRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin
public class AdminUserController {

    private final UserRepository userRepository;
    private final ChildProfileRepository childProfileRepository;
    private final ParentChildRepository parentChildRepository;


    public AdminUserController(UserRepository userRepository,
                               ChildProfileRepository childProfileRepository,
                               ParentChildRepository parentChildRepository) {
        this.userRepository = userRepository;
        this.childProfileRepository = childProfileRepository;
        this.parentChildRepository = parentChildRepository;
    }


    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name() // enum -> String
                ))
                .toList();
    }


    @DeleteMapping("/{id}")
    @Transactional
    public String deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return "User not found";
        }

        try {
            User user = userRepository.findById(id).orElseThrow();

            // 👉 If CHILD → delete relationships first
            if (user.getRole().name().equals("CHILD")) {

                childProfileRepository.findByUserId(user.getId())
                        .ifPresent(child -> {

                            // delete from parent_child table first
                            parentChildRepository.deleteByChildId(child.getId());

                            // then delete child profile
                            childProfileRepository.delete(child);
                        });
            }

            // 👉 Finally delete user
            userRepository.deleteById(id);

            return "User deleted successfully";

        } catch (Exception e) {
            e.printStackTrace(); // important for debugging
            return "Cannot delete user due to related data";
        }
    }
}