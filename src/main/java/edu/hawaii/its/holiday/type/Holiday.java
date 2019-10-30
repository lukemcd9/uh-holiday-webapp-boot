package edu.hawaii.its.holiday.type;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import edu.hawaii.its.holiday.util.Dates;

@JsonRootName(value = "data")
public class Holiday implements Serializable {

    private static final long serialVersionUID = 53L;

    @JsonIgnore
    private Integer id;

    @JsonIgnore
    private Integer version;

    private String description;

    @JsonSerialize(using = HolidayDateSerializer.class)
    private LocalDate observedDate;

    @JsonSerialize(using = HolidayDateSerializer.class)
    private LocalDate officialDate;

    private List<Type> types = new ArrayList<>(0);

    private boolean closest;

    // Constructor.
    public Holiday() {
        this.closest = false;
        // Empty.
    }

    // Constructor.
    public Holiday(LocalDate officialDate, LocalDate observedDate) {
        this();
        this.officialDate = officialDate;
        this.observedDate = observedDate;
    }

    public boolean isClosest() { return closest;}

    public void setClosest(boolean closest) {
        this.closest = closest;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonGetter("observedDateFull")
    public LocalDate getObservedDate() {
        return observedDate;
    }

    public void setObservedDate(LocalDate observedDate) {
        this.observedDate = observedDate;
    }

    @JsonGetter("officialDateFull")
    public LocalDate getOfficialDate() {
        return officialDate;
    }

    public void setOfficialDate(LocalDate officialDate) {
        this.officialDate = officialDate;
    }

    @JsonGetter("observedDate")
    public String getObservedDateStr() {
        return Dates.formatDate(observedDate, "yyyy-MM-dd");
    }

    @JsonGetter("officialDate")
    public String getOfficialDateStr() {
        return Dates.formatDate(officialDate, "yyyy-MM-dd");
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types != null ? types : new ArrayList<>();
    }

    public List<String> getHolidayTypes() {
        return types.stream().map(Type::getDescription).collect(Collectors.toList());
    }

    public Integer getYear() {
        if (observedDate != null) {
            return Dates.yearOfDate(observedDate);
        }
        return null;
    }

    public Integer getOfficialYear() {
        if (officialDate != null) {
            return Dates.yearOfDate(officialDate);
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((observedDate == null) ? 0 : observedDate.hashCode());
        result = prime * result + ((officialDate == null) ? 0 : officialDate.hashCode());
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
        Holiday other = (Holiday) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (observedDate == null) {
            if (other.observedDate != null)
                return false;
        } else if (!observedDate.equals(other.observedDate))
            return false;
        if (officialDate == null) {
            if (other.officialDate != null)
                return false;
        } else if (!officialDate.equals(other.officialDate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Holiday ["
                + "id=" + id
                + ", description=" + description
                + ", observedDate=" + observedDate
                + ", officialDate=" + officialDate
                + ", types=" + types
                + "]";
    }

}
