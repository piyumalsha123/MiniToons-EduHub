package lk.ijse.minitoonseduhub.service;

import lk.ijse.minitoonseduhub.dto.CartoonDto;
import lk.ijse.minitoonseduhub.entity.Cartoon;
import lk.ijse.minitoonseduhub.repository.CartoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartoonService {

    private final CartoonRepository cartoonRepository;


    private CartoonDto toDto(Cartoon cartoon) {
        return CartoonDto.builder()
                .cartoonId(cartoon.getCartoonId())
                .title(cartoon.getTitle())
                .category(cartoon.getCategory())
                .videoUrl(cartoon.getVideoUrl())
                .ageGroup(cartoon.getAgeGroup())
                .status(cartoon.getStatus())
                .createdAt(cartoon.getCreatedAt())
                .build();
    }

    private String generateCartoonId() {
        long count = cartoonRepository.count() + 1;
        return String.format("C%03d", count);
    }

    public CartoonDto createCartoon(CartoonDto dto) {
        Cartoon cartoon = Cartoon.builder()
                .cartoonId(generateCartoonId())
                .title(dto.getTitle())
                .category(dto.getCategory())
                .videoUrl(dto.getVideoUrl())
                .ageGroup(dto.getAgeGroup())
                .status(dto.getStatus() != null ? dto.getStatus() : "Draft")
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDate.now())
                .build();
        return toDto(cartoonRepository.save(cartoon));
    }

    public List<CartoonDto> getAllCartoons() {
        return cartoonRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CartoonDto getCartoonById(String cartoonId) {
        Cartoon cartoon = cartoonRepository.findById(cartoonId)
                .orElseThrow(() -> new RuntimeException("Cartoon not found for ID: " + cartoonId));
        return toDto(cartoon);
    }

    public CartoonDto updateCartoon(String cartoonId, CartoonDto dto) {
        Cartoon cartoon = cartoonRepository.findById(cartoonId)
                .orElseThrow(() -> new RuntimeException("Cartoon not found for ID: " + cartoonId));

        cartoon.setTitle(dto.getTitle());
        cartoon.setCategory(dto.getCategory());
        cartoon.setVideoUrl(dto.getVideoUrl());
        cartoon.setAgeGroup(dto.getAgeGroup());
        cartoon.setStatus(dto.getStatus() != null ? dto.getStatus() : cartoon.getStatus());
        cartoon.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : cartoon.getCreatedAt());

        return toDto(cartoonRepository.save(cartoon));
    }

    public void deleteCartoon(String cartoonId) {
        Cartoon cartoon = cartoonRepository.findById(cartoonId)
                .orElseThrow(() -> new RuntimeException("Cartoon not found for ID: " + cartoonId));
        cartoonRepository.delete(cartoon);
    }
}