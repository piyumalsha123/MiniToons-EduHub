package lk.ijse.minitoonseduhub.service;

import jakarta.transaction.Transactional;
import lk.ijse.minitoonseduhub.dto.ChildCreateDto;
import lk.ijse.minitoonseduhub.dto.ChildResponseDto;
import lk.ijse.minitoonseduhub.dto.ChildUpdateDto;
import lk.ijse.minitoonseduhub.entity.ChildProfile;
import lk.ijse.minitoonseduhub.entity.ParentChild;
import lk.ijse.minitoonseduhub.entity.Role;
import lk.ijse.minitoonseduhub.entity.User;
import lk.ijse.minitoonseduhub.repository.ChildProfileRepository;
import lk.ijse.minitoonseduhub.repository.ParentChildRepository;
import lk.ijse.minitoonseduhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChildService {

    private final UserRepository userRepository;
    private final ChildProfileRepository childProfileRepository;
    private final ParentChildRepository parentChildRepository;
    private final PasswordEncoder passwordEncoder;


    public String createChild(ChildCreateDto dto, String parentUsername) {

        User parent = userRepository.findByUsername(parentUsername)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User childUser = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.CHILD)
                .build();
        userRepository.save(childUser);

        ChildProfile profile = ChildProfile.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .user(childUser)
                .build();
        childProfileRepository.save(profile);

        ParentChild relation = ParentChild.builder()
                .parent(parent)
                .child(profile)
                .build();
        parentChildRepository.save(relation);

        return "Child created successfully";

    }


    public List<ChildResponseDto> getChildrenByParent(String parentUsername) {

        // 1. Username එකෙන් ලොග් වෙලා ඉන්න Parent ව හොයාගන්නවා
        User parent = userRepository.findByUsername(parentUsername)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        // 2. ඒ Parent ට අදාළ ParentChild records ටික විතරක් ගන්නවා
        List<ParentChild> relations = parentChildRepository.findByParent(parent);

        // 3. ඒ relations වලින් ChildProfile ටික විතරක් අරන් DTO එකකට map කරනවා
        return relations.stream()
                .map(relation -> {
                    ChildProfile c = relation.getChild();
                    String username = c.getUser() != null ? c.getUser().getUsername() : "N/A";
                    return new ChildResponseDto(c.getId(), c.getName(), c.getAge(), c.getGender(), username);
                })
                .toList();
    }


    public void deleteChild(Long childId) {
        ChildProfile profile = childProfileRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        parentChildRepository.deleteByChild(profile);
        childProfileRepository.delete(profile);
        userRepository.delete(profile.getUser());
    }

    public void updateChild(Long childId, ChildUpdateDto dto, String parentUsername) {

        User parent = userRepository.findByUsername(parentUsername)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        ChildProfile child = childProfileRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        boolean belongsToParent = parentChildRepository.existsByParentAndChild(parent, child);
        if (!belongsToParent) {
            throw new RuntimeException("Unauthorized");
        }


        if (dto.getName() != null) child.setName(dto.getName());
        if (dto.getAge() != null) child.setAge(dto.getAge());
        if (dto.getGender() != null) child.setGender(dto.getGender());

        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            User childUser = child.getUser();


            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            childUser.setUsername(dto.getUsername());
            userRepository.save(childUser);
        }


        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            User childUser = child.getUser();
            childUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.save(childUser);
        }

        childProfileRepository.save(child);
    }
}