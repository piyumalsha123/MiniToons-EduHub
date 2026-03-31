package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.SongDto;
import lk.ijse.minitoonseduhub.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class SongController {

    private final SongService songService;

    @PostMapping("/save")
    public SongDto create(@RequestBody SongDto dto) {
        return songService.createSong(dto);
    }

    @GetMapping
    public List<SongDto> getAll() {
        return songService.getAllSongs();
    }

    @GetMapping("/{songId}")
    public SongDto getSongById(@PathVariable String songId) {
        return songService.getSongById(songId);
    }

    @PutMapping("/update/{songId}")
    public SongDto update(@PathVariable String songId, @RequestBody SongDto dto) {
        return songService.updateSongBySongId(songId, dto);
    }

    @DeleteMapping("/delete/{songId}")
    public void delete(@PathVariable String songId) {
        songService.deleteSongBySongId(songId);
    }
}