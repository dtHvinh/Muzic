package com.dthvinh.Server.DTOs;

import com.dthvinh.Server.Contract.SelfValidate;
import com.dthvinh.Server.Types.ValidationResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateSongDto(String title, @JsonProperty("artist_id") Long artistId,
                            @JsonProperty("spotify_id") String spotifyId,
                            @JsonProperty("audio_url") String audioUrl) implements SelfValidate {
    @Override
    public ValidationResult validate() {
        if (artistId() == null || title() == null || title().isBlank()) {
            return ValidationResult.error("title and artist_id are required");
        }

        return ValidationResult.success();
    }
}
