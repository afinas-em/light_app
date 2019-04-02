package tech.bottleneck.the_lightapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DownloadTable extends RealmObject {

    @PrimaryKey
    private String id;
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
