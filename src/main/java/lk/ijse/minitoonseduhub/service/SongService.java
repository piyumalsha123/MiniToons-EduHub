package lk.ijse.minitoonseduhub.service;

import lk.ijse.minitoonseduhub.dto.SongDto;
import lk.ijse.minitoonseduhub.entity.Song;
import lk.ijse.minitoonseduhub.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;


    private SongDto toDto(Song song) {
        return SongDto.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .videoUrl(song.getVideoUrl())
                .lyrics(song.getLyrics())
                .ageGroup(song.getAgeGroup())
                .status(song.getStatus())
                .createdAt(song.getCreatedAt())
                .build();
    }

    private String generateSongId() {
        Long count = songRepository.count() + 1;
        return String.format("S%03d", count);
    }

    public SongDto createSong(SongDto dto) {

        Song song = Song.builder()
                .songId(generateSongId())
                .title(dto.getTitle())
                .videoUrl(dto.getVideoUrl())
                .lyrics(dto.getLyrics())
                .ageGroup(dto.getAgeGroup())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDate.now())
                .status(dto.getStatus() != null ? dto.getStatus() : "Active")
                .build();

        Song saved = songRepository.save(song);
        return toDto(saved);
    }


    public List<SongDto> getAllSongs() {
        return songRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public SongDto getSongById(String id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        return toDto(song);
    }


    public SongDto updateSongBySongId(String songId, SongDto dto) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found for ID: " + songId));

        song.setTitle(dto.getTitle());
        song.setVideoUrl(dto.getVideoUrl());
        song.setLyrics(dto.getLyrics());
        song.setAgeGroup(dto.getAgeGroup());
        song.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : song.getCreatedAt());
        song.setStatus(dto.getStatus() != null ? dto.getStatus() : song.getStatus());

        return toDto(songRepository.save(song));
    }


    public void deleteSongBySongId(String songId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found for ID: " + songId));

        songRepository.delete(song);
    }
}