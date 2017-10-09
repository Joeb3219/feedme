package me.josephboyle.feedme.tools;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GoogleMapper {

	@SerializedName("debug_info")
    public List<String> debug_info;

    @SerializedName("html_attributions")
    public List<String> html_attributions;

    @SerializedName("next_page_token")
    public String next_page_token;

    @SerializedName("results")
    public List<Results> results;

    @SerializedName("status")
    public String status;
	
}
