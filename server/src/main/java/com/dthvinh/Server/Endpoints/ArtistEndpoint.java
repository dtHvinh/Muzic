package com.dthvinh.Server.Endpoints;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dthvinh.Server.DTOs.ArtistResponseDto;
import com.dthvinh.Server.DTOs.CreateArtistDto;
import com.dthvinh.Server.DTOs.UpdateArtistDto;
import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.sun.net.httpserver.HttpExchange;

/**
 *
 * @author dthvinh
 */
@Endpoint(route = "artists")
public class ArtistEndpoint extends BaseEndpoint {
    private final ArtistRepository artistRepository = ArtistRepository.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (isGet(exchange)) {
            handleGetArtist(exchange);
        } else if (isPost(exchange)) {
            handleCreateArtist(exchange);
        } else if (isDelete(exchange)) {
            handleDeleteArtist(exchange);
        } else if (isPut(exchange)) {
            handleUpdateArtist(exchange);
        } else {
            sendBadRequest(exchange, "Endpoint not found");
        }
    }

    private void handleGetArtist(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryParams(exchange);

        if (params.containsKey("id")) {
            Artist artist = artistRepository.findById(Long.valueOf(params.get("id")))
                    .orElse(null);

            if (artist == null) {
                sendNotFound(exchange);
                return;
            }

            sendOk(exchange, artist);
            return;
        }

        List<ArtistResponseDto> artists = artistRepository
                .findAll(params.get("query"),
                        Integer.valueOf(params.get("limit")),
                        Integer.valueOf(params.get("offset")))
                .stream()
                .map(ArtistResponseDto::from)
                .toList();

        logger.Console("There is {%d} artist match query \"%s\"".formatted(artists.size(), params.get("name")));

        sendOk(exchange, Map.of("artists", artists));
    }

    private void handleCreateArtist(HttpExchange exchange) throws IOException {
        CreateArtistDto dto = parseBody(exchange, CreateArtistDto.class);

        Artist artist = new Artist(null,
                dto.name(),
                dto.bio(),
                dto.profileImage(),
                dto.spotifyId(),
                null,
                null);
        Artist created = artistRepository.save(artist);

        sendOk(exchange, Map.of("id", created.getId()));
    }

    private void handleDeleteArtist(HttpExchange exchange) throws IOException {
        Map<String, String> a = parseQueryParams(exchange);

        Long id = Long.valueOf(a.get("id"));
        if (artistRepository.delete(id)) {
            sendOk(exchange, Map.of("id", id));
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleUpdateArtist(HttpExchange exchange) throws IOException {
        Long id = Long.valueOf(parseQueryParams(exchange).get("id"));
        UpdateArtistDto dto = parseBody(exchange, UpdateArtistDto.class);

        Optional<Artist> existArtist = artistRepository.findById(id);
        if (existArtist.isEmpty()) {
            sendNotFound(exchange);
            return;
        }

        Artist current = artistRepository.findById(id)
                .orElse(null);
        if (current == null) {
            sendNotFound(exchange);
            return;
        }

        Artist updated = new Artist(
                current.getId(),
                dto.name() != null ? dto.name() : current.getName(),
                dto.bio() != null ? dto.bio() : current.getBio(),
                dto.profileImage() != null ? dto.profileImage() : current.getProfileImage(),
                dto.spotifyId() != null ? dto.spotifyId() : current.getSpotifyId(),
                current.getCreatedAt(),
                LocalDateTime.now());

        Artist saved = artistRepository.update(id, updated);
        sendOk(exchange, ArtistResponseDto.from(saved));
    }
}
