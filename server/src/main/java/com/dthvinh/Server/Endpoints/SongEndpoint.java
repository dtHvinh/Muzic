package com.dthvinh.Server.Endpoints;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.dthvinh.Server.DTOs.CreateSongDto;
import com.dthvinh.Server.DTOs.SongResponseDto;
import com.dthvinh.Server.DTOs.UpdateSongDto;
import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Models.Song;
import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.Repositories.SongRepository;
import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.sun.net.httpserver.HttpExchange;

@Endpoint(route = "songs")
public class SongEndpoint extends BaseEndpoint {
    private final SongRepository songs;
    private final ArtistRepository artists;

    public SongEndpoint(SongRepository songs, ArtistRepository artists) {
        this.songs = songs;
        this.artists = artists;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (isGet(exchange)) {
            handleGet(exchange);
        } else if (isPost(exchange)) {
            handleCreate(exchange);
        } else if (isPut(exchange)) {
            handleUpdate(exchange);
        } else if (isDelete(exchange)) {
            handleDelete(exchange);
        } else {
            sendBadRequest(exchange, "Unsupported verb");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        if (params.containsKey("id")) {
            handleGetSongById(exchange);
            return;
        }

        List<SongResponseDto> list = songs
                .findAll(params.get("title"),
                        params.get("limit") == null ? null : Integer.valueOf(params.get("limit")),
                        params.get("offset") == null ? null : Integer.valueOf(params.get("offset")))
                .stream()
                .map(s -> {
                    String artistName = artists.findById(s.getArtistId())
                            .map(Artist::getName)
                            .orElse(null);
                    return SongResponseDto.from(s, artistName);
                })
                .toList();

        logger.info("There is {%d} song match query \"%s\"".formatted(list.size(), params.get("title")));

        sendOk(exchange, Map.of("songs", list));
    }

    private void handleGetSongById(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        SongResponseDto song = songs.findById(Long.valueOf(params.get("id")))
                .map(s -> {
                    String artistName = artists.findById(s.getArtistId())
                            .map(Artist::getName)
                            .orElse(null);
                    return SongResponseDto.from(s, artistName);
                })
                .orElse(null);
        if (song == null) {
            sendNotFound(exchange);
            return;
        }

        sendOk(exchange, song);
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        CreateSongDto dto = parseBody(exchange, CreateSongDto.class);
        if (dto == null || !dto.validate().success) {
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

        Song saved = songs.update(id, current.with(dto));

        sendOk(exchange, SongResponseDto.from(saved));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        if (!params.containsKey("id") || params.get("id") == null || params.get("id").isBlank()) {
            sendBadRequest(exchange, "id is required");
            return;
        }

        final Long id;
        try {
            id = Long.valueOf(params.get("id"));
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "id must be a number");
            return;
        }

        try {
            if (songs.delete(id)) {
                sendOk(exchange, Map.of("id", id));
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendBadRequest(exchange, "Failed to delete song");
        }
    }
}
