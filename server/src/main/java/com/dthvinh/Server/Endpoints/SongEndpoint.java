package com.dthvinh.Server.Endpoints;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.dthvinh.Server.DTOs.CreateSongDto;
import com.dthvinh.Server.DTOs.SongResponseDto;
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
                    dto.title(),
                    dto.artistId(),
                    dto.spotifyId()));
        } catch (Exception e) {
            return;
        }

        sendOk(exchange, Map.of("id", created.getId()));
    }
}
