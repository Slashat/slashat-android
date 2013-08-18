package se.slashat.slashapp.model;

import org.joda.time.DateTime;

import java.io.Serializable;

public class LiveEvent implements Comparable<LiveEvent>, Serializable {
    private static final long serialVersionUID = 1L;
    private final DateTime start;
    private final DateTime end;
    private final String summary;
    private final String description;

    public LiveEvent(DateTime start, DateTime end, String summary, String description) {
        super();
        this.start = start;
        this.end = end;
        this.summary = summary;
        this.description = description;
    }

    public DateTime getEnd() {
        return end;
    }

    public DateTime getStart() {
        return start;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((summary == null) ? 0 : summary.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LiveEvent other = (LiveEvent) obj;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (summary == null) {
            if (other.summary != null)
                return false;
        } else if (!summary.equals(other.summary))
            return false;
        return true;
    }

    @Override
    public int compareTo(LiveEvent l2) {
        return l2.getStart().compareTo(start);
    }
}
