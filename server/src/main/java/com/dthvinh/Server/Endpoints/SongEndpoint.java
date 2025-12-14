package com.dthvinh.Server.Endpoints;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.dthvinh.Server.DTOs.CreateSongDto;
import com.dthvinh.Server.DTOs.SongResponseDto;
import com.dthvinh.Server.DTOs.UpdateSongDto;
import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.Models.Song;
import com.dthvinh.Server.Repositories.SongRepository;
import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.sun.net.httpserver.HttpExchange;

@Endpoint(route = "songs")
public class SongEndpoint extends BaseEndpoint {
    private final SongRepository songs = SongRepository.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (isGet(exchange)) {
            handleGet(exchange);
        } else if (isPost(exchange)) {
            handleCreate(exchange);
        } else if (isPut(exchange)) {
            handleUpdate(exchange);
        } else {
            sendBadRequest(exchange, "Unsupported verb");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        if (params.containsKey("id")) {
            var song = songs.findById(Long.valueOf(params.get("id")))
                    .map(SongResponseDto::from)
                    .orElse(null);
            if (song == null) {
                sendNotFound(exchange);
                return;
            }
            sendOk(exchange, song);
            return;
        }

        List<SongResponseDto> list = songs
                .findAll(params.get("title"),
                        params.get("limit") == null ? null : Integer.valueOf(params.get("limit")),
                        params.get("offset") == null ? null : Integer.valueOf(params.get("offset")))
                .stream()
                .map(SongResponseDto::from)
                .toList();

        sendOk(exchange, Map.of("songs", list));
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        CreateSongDto dto = parseBody(exchange, CreateSongDto.class);
        if (dto == null || dto.artistId() == null || dto.title() == null || dto.title().isBlank()) {
            sendBadRequest(exchange, "title and artist_id are required");
            return;
        }

        Song created;
        try {
            created = songs.save(new Song(
                    dto.title().trim(),
                    dto.artistId(),
                    dto.spotifyId(),
                    dto.audioUrl()));
        } catch (Exception e) {
            sendBadRequest(exchange, "Failed to create song");
            return;
        }

        sendOk(exchange, SongResponseDto.from(created));
    }

    private void handleUpdate(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        if (!params.containsKey("id")) {
            sendBadRequest(exchange, "id is required");
            return;
        }

        Long id = Long.valueOf(params.get("id"));
        UpdateSongDto dto = parseBody(exchange, UpdateSongDto.class);

        Song current = songs.findById(id).orElse(null);
        if (current == null) {
            sendNotFound(exchange);
            return;
        }

        Song updated = new Song(
                current.getId(),
                dto.title() != null && !dto.title().isBlank() ? dto.title().trim() : current.getTitle(),
                dto.artistId() != null ? dto.artistId() : current.getArtistId(),
                dto.spotifyId() != null && !dto.spotifyId().isBlank() ? dto.spotifyId().trim() : current.getSpotifyId(),
                dto.audioUrl() != null && !dto.audioUrl().isBlank() ? dto.audioUrl().trim() : current.getAudioUrl(),
                current.getCreatedAt(),
                LocalDateTime.now());

        Song saved = songs.update(id, updated);
        sendOk(exchange, SongResponseDto.from(saved));
    }
}
