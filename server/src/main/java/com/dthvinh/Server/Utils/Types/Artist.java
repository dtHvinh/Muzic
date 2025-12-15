package com.dthvinh.Server.Utils.Types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Artist {

    public String id;
    public String type;
    @JsonProperty("begin_area")
    public String typeId;
    public int score;
    @JsonProperty("begin_area")
    public String genderId;
    public String name;
    @JsonProperty("begin_area")
    public String sortName;
    public String gender;
    public String country;

    public Area area;
    @JsonProperty("begin_area")
    public Area begin_area;

    public String disambiguation;
    public List<String> ipis;
    public List<String> isnis;

    @JsonProperty("life_span")
    public LifeSpan lifespan;

    public List<Alias> aliases;
    public List<Tag> tags;
}
