package me.josephboyle.feedme.tools;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Photos {

	@SerializedName("height")
    public int height;

    @SerializedName("width")
    public int width;

    @SerializedName("html_attributions")
    public List<String> html_attributions;

    @SerializedName("photo_reference")
    public String photo_reference;
	
}
