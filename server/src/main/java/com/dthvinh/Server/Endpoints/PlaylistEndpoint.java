package com.dthvinh.Server.Endpoints;

import com.dthvinh.Server.DTOs.*;
import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.Models.Playlist;
import com.dthvinh.Server.Repositories.PlaylistRepository;
import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Endpoint(route = "playlists")
public class PlaylistEndpoint extends BaseEndpoint {
    private final PlaylistRepository repository = PlaylistRepository.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(isGet(exchange)){
            handleGetPlayList(exchange);
        } else if (isPost(exchange)){
            handleCreatePlaylist(exchange);
        } else if (isPut(exchange)){
            handleUpdatePlaylist(exchange);
        } else if (isDelete(exchange)){
            handleDeletePlaylist(exchange);
        }
    }

    private void handleDeletePlaylist(HttpExchange exchange) throws IOException {
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
            if (repository.delete(id)) {
                sendOk(exchange, Map.of("id", id));
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendBadRequest(exchange, "Failed to delete playlist");
        }
    }

    private void handleUpdatePlaylist(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        if (!params.containsKey("id")) {
            sendBadRequest(exchange, "id is required");
            return;
        }

        Long id = Long.valueOf(params.get("id"));

        UpdatePlaylistDto updatePlaylistDto = parseBody(exchange, UpdatePlaylistDto.class);

        Playlist current = repository.findById(id).orElse(null);
        if (current == null) {
            sendNotFound(exchange);
            return;
        }

        Playlist updated = new Playlist(id,
                updatePlaylistDto.name(),
                updatePlaylistDto.description(),
                LocalDateTime.now());

        Playlist saved = repository.update(id, updated);
        sendOk(exchange, PlaylistResponseDto.from(saved));
    }

    private void handleGetPlayList(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);
        if(params.containsKey("mode")){
            if(params.get("mode").equalsIgnoreCase("details")) {
                handleGetPlaylistDetails(exchange);
                return;
            }else if(params.get("mode").equalsIgnoreCase("add-song")){
                handleAddSong(exchange);
                return;
            }
            else{
                sendBadRequest(exchange, "mode not supported");
            }
        }

        if (params.containsKey("id")) {
            Playlist playlist = repository.findById(Long.valueOf(params.get("id")))
                    .orElse(null);

            if (playlist == null) {
                sendNotFound(exchange);
                return;
            }

            sendOk(exchange, playlist);
            return;
        }

        List<PlaylistResponseDto> playlists = repository
                .findAll(params.getOrDefault("name", null),
                        Integer.valueOf(params.get("limit")),
                        Integer.valueOf(params.get("offset")))
                .stream()
                .map(PlaylistResponseDto::from)
                .toList();

        logger.Console("There is {%d} artist match query \"%s\"".formatted(playlists.size(), params.get("name")));

        sendOk(exchange, Map.of("playlists", playlists));
    }

    private void handleGetPlaylistDetails(HttpExchange exchange) {
        Long playlistId = Long.valueOf(parseQueryParams(exchange).get("id"));


    }

    private  void handleAddSong(HttpExchange exchange) throws IOException {
        Long playlistId = Long.valueOf(parseQueryParams(exchange).get("playlistId"));
        Long songId = Long.valueOf(parseQueryParams(exchange).get("songId"));

        repository.addToPlaylist(playlistId, songId);

        logger.Console("Add song #{%d} to playlist #{%d}".formatted(songId, playlistId));

        sendOk(exchange, Map.of("playlistId", playlistId, "songId", songId));
    }

    private void handleCreatePlaylist(HttpExchange exchange) throws IOException {
        CreatePlaylistDto dto = parseBody(exchange, CreatePlaylistDto.class);

        if (dto == null || dto.name() == null) {
            sendBadRequest(exchange, "name is required");
            return;
        }

        Playlist created;

        try {
            created = repository.save(new Playlist(dto.name(), dto.description()));
        } catch (Exception e) {
            sendBadRequest(exchange, "Failed to create song");
            return;
        }

        sendOk(exchange, Map.of("id", created.getId()));
    }
}
