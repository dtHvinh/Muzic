package com.dthvinh.Server.Utils.Builder;

public class MusicBrainUrlBuilder {
    public String artistName;
    public int limit;
    public int offset;

    private MusicBrainUrlBuilder() {}

    public static MusicBrainUrlBuilder createBuilder(){
        return new MusicBrainUrlBuilder();
    }

    public MusicBrainUrlBuilder withQueryArtistName(String queryArtistName){
        this.artistName = queryArtistName;
        return this;
    }

    public MusicBrainUrlBuilder withLimit(int limit){
        this.limit = limit;
        return this;
    }

    public MusicBrainUrlBuilder withOffset(int offset){
        this.offset = offset;
        return this;
    }

    public String buildSearchArtist(){
        StringBuilder sb = new StringBuilder();
        sb.append("artist");
        appendSearchQuery(sb, "query", artistName ,true);
        appendSearchQuery(sb, "limit", limit ,false);
        appendSearchQuery(sb, "offset", offset ,false);

        return sb.toString();
    }

    private void appendSearchQuery(StringBuilder stringBuilder, String query, Object value, boolean first){
        if(value == null){
            return;
        }

        stringBuilder.append(first ? "?" : "&");
        stringBuilder.append(query);
        stringBuilder.append("=");
        stringBuilder.append(value);
    }
}
