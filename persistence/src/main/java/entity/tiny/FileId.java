package entity.tiny;

/**
 *
 */
public class FileId {

    private long id;

    public FileId(long id) {
        this.id = id;
    }

    public long get() {
        return id;
    }

    public void set(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileId fileId = (FileId) o;

        return id == fileId.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
