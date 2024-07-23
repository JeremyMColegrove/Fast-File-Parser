package core;

public class Pointer {
    private Page page;
    private Object location;
    private Integer offset;

    public Page getPage() {
        return page;
    }

    public Object getLocation() {
        return location;
    }

    public Integer getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "core.Pointer{" +
                "page=" + page +
                ", location='" + location + '\'' +
                ", offset=" + offset +
                '}';
    }

    public Pointer(Page page, Object location, Integer offset) {
        this.page = page;
        this.location = location;
        this.offset = offset;
    }
}
