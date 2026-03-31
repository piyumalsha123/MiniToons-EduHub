package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.CartoonDto;
import lk.ijse.minitoonseduhub.service.CartoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cartoons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class CartoonController {

    private final CartoonService cartoonService;

    @PostMapping("/save")
    public CartoonDto create(@RequestBody CartoonDto dto) {
        return cartoonService.createCartoon(dto);
    }

    @GetMapping
    public List<CartoonDto> getAll() {
        return cartoonService.getAllCartoons();
    }

    @GetMapping("/{cartoonId}")
    public CartoonDto getById(@PathVariable String cartoonId) {
        return cartoonService.getCartoonById(cartoonId);
    }

    @PutMapping("/update/{cartoonId}")
    public CartoonDto update(@PathVariable String cartoonId, @RequestBody CartoonDto dto) {
        return cartoonService.updateCartoon(cartoonId, dto);
    }

    @DeleteMapping("/delete/{cartoonId}")
    public void delete(@PathVariable String cartoonId) {
        cartoonService.deleteCartoon(cartoonId);
    }
}