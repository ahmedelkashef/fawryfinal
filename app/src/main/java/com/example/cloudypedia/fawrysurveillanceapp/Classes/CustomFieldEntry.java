package com.example.cloudypedia.fawrysurveillanceapp.Classes;


public class CustomFieldEntry {

    public String displayName;
    public String predefinedValues;
    public boolean indexed;

    public CustomFieldEntry() {
    }

    public CustomFieldEntry(String displayName,
                            String predefinedValues,
                            boolean indexed) {

        this.displayName = displayName;
        this.predefinedValues = predefinedValues;
        this.indexed = indexed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPredefinedValues() {
        return predefinedValues;
    }

    public void setPredefinedValues(String predefinedValues) {
        this.predefinedValues = predefinedValues;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    @Override
    public int hashCode() {
        return displayName.hashCode() ^ predefinedValues.hashCode() ^ new Boolean(indexed).hashCode();
    }
}
