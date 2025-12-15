package com.dthvinh.Server.Utils.Types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Area {
    public String id;
    public String type;
    @JsonProperty("type_id")
    public String typeId;
    public String name;
    @JsonProperty("sort_name")
    public String sortName;
    @JsonProperty("life_span")
    public LifeSpan lifeSpan;
}