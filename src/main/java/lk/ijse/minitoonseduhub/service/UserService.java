package lk.ijse.minitoonseduhub.service;

import jakarta.transaction.Transactional;
import lk.ijse.minitoonseduhub.dto.AuthDto;
import lk.ijse.minitoonseduhub.dto.AuthResponseDto;
import lk.ijse.minitoonseduhub.dto.RegisterDto;
import lk.ijse.minitoonseduhub.entity.Role;
import lk.ijse.minitoonseduhub.entity.User;
import lk.ijse.minitoonseduhub.repository.ChildProfileRepository;
import lk.ijse.minitoonseduhub.repository.UserRepository;
import lk.ijse.minitoonseduhub.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ChildProfileRepository childProfileRepository;

    public String saveUser(RegisterDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Role role;
        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role (ADMIN, PARENT, CHILD only)");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        return "User Registered Successfully";
    }


    public AuthResponseDto authenticate(AuthDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                "ROLE_" + user.getRole().name()
        );

        // ⭐ Default values ටික හදාගන්නවා
        Long id = null; // 👈 🚨 ID එකට අලුතින් variable එකක් හදනවා
        String name = user.getUsername();
        String gender = "MALE";

        // ලොග් වෙලා ඉන්නේ CHILD කෙනෙක් නම්, ඒ User ට අදාළ Child Profile එක හොයනවා
        if (user.getRole() == Role.CHILD) {
            var profile = childProfileRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Child profile not found"));

            id = profile.getId(); // 👈 🚨 මෙන්න ළමයාගේ ඇත්තම ID එක (20) ගන්නවා!
            name = profile.getName();
            gender = profile.getGender();
        }

        // 🚀 [අවසාන Response එක]
        // Token එක, ID එක, නම සහ Gender එක හතරම දැන් යනවා!
        return new AuthResponseDto(token, id, name, gender);
    }
    }