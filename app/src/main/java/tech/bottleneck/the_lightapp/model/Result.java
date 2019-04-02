package tech.bottleneck.the_lightapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {

    private String duration;

    private String unit;

    private String pdf;

    private String module;

    private String description;

    private String id;

    private String video;

    private String audio;

    private String category;

    private String title;

    protected Result(Parcel in) {
        duration = in.readString();
        unit = in.readString();
        pdf = in.readString();
        module = in.readString();
        description = in.readString();
        id = in.readString();
        video = in.readString();
        audio = in.readString();
        category = in.readString();
        title = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getUnit ()
    {
        return unit;
    }

    public void setUnit (String unit)
    {
        this.unit = unit;
    }

    public String getPdf ()
    {
        return pdf;
    }

    public void setPdf (String pdf)
    {
        this.pdf = pdf;
    }

    public String getModule ()
    {
        return module;
    }

    public void setModule (String module)
    {
        this.module = module;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getVideo ()
    {
        return video;
    }

    public void setVideo (String video)
    {
        this.video = video;
    }

    public String getAudio ()
    {
        return audio;
    }

    public void setAudio (String audio)
    {
        this.audio = audio;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeString(unit);
        dest.writeString(pdf);
        dest.writeString(module);
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(category);
        dest.writeString(title);
    }
}
