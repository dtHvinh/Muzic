package com.dthvinh.Server.Utils.Types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Alias {
    @JsonProperty("sort_name")
    public String sortName;
    @JsonProperty("type_id")
    public String typeId;
    public String name;
    public String locale;
    public String type;
    public Boolean primary;
    @JsonProperty("begin_date")
    public String beginDate;
    @JsonProperty("end_date")
    public String endDate;
}
