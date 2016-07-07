package entity;

import entity.tiny.FileId;

import java.util.Arrays;

/**
 *
 */
public class FileContent {

    private byte[] content;

    private FileId fileId;

    public FileContent(byte[] content, FileId fileId) {
        this.content = content;
        this.fileId = fileId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public FileId getFileId() {
        return fileId;
    }

    public void setFileId(FileId fileId) {
        this.fileId = fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileContent that = (FileContent) o;

        if (!Arrays.equals(content, that.content)) return false;
        return fileId != null ? fileId.equals(that.fileId) : that.fileId == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(content);
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        return result;
    }
}
