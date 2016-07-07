package entity;

import entity.tiny.FileExtension;
import entity.tiny.FileId;
import entity.tiny.LocationId;
import entity.tiny.UserId;

/**
 * File entity.
 */
public class File {

    private FileId id;

    private LocationId locationId;

    private String name;

    private FileExtension extension;

    private UserId ownerId;

    public File(LocationId locationId, String name, FileExtension extension, UserId ownerId) {
        this.id = new FileId(0);
        this.locationId = locationId;
        this.name = name;
        this.extension = extension;
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (!id.equals(file.id)) return false;
        if (!locationId.equals(file.locationId)) return false;
        if (!name.equals(file.name)) return false;
        if (extension != null ? !extension.equals(file.extension) : file.extension != null) return false;
        return ownerId.equals(file.ownerId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + locationId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + ownerId.hashCode();
        return result;
    }

    public FileId getId() {
        return id;
    }

    public void setId(FileId id) {
        this.id = id;
    }

    public LocationId getLocationId() {
        return locationId;
    }

    public void setLocationId(LocationId locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileExtension getExtension() {
        return extension;
    }

    public void setExtension(FileExtension extension) {
        this.extension = extension;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UserId ownerId) {
        this.ownerId = ownerId;
    }
}
