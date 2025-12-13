package com.dthvinh.Server.Service;

import com.dthvinh.Server.Utils.Builder.MusicBrainUrlBuilder;
import com.dthvinh.Server.Utils.Types.ArtistSearchPaginationResponse;
import com.dthvinh.Server.Utils.Types.Base.PaginationParams;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MusicAPIService {
    private final String API_ROOT_URL= "https://musicbrainz.org/ws/2/";
    private final HttpClient client;

    private static MusicAPIService instance;

    public MusicAPIService() {
        client = HttpClient.newHttpClient();
    }

    public static MusicAPIService getInstance(){
        if(instance==null){
            instance = new MusicAPIService();
        }

        return instance;
    }

    public ArtistSearchPaginationResponse findArtistByName(
            String name,
            PaginationParams paginationParams
    ) throws IOException, InterruptedException {

        String queryString = MusicBrainUrlBuilder.createBuilder()
                .withQueryArtistName(name)
                .withOffset(paginationParams.offset)
                .withLimit(paginationParams.limit)
                .buildSearchArtist();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(API_ROOT_URL, queryString)))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, ArtistSearchPaginationResponse.class);
    }
}


