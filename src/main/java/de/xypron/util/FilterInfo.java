package de.xypron.util;

public class FilterInfo implements Comparable<FilterInfo> {

    private Integer column;
    private Filter.ComparisonType comparisonType;
    private String value;
    private boolean isNumeric;

    public FilterInfo(int column) {
        this.column = column;
        comparisonType = Filter.ComparisonType.IGNORE;
        value = "";
        isNumeric = false;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        this.isNumeric = numeric;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Filter.ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(Filter.ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(FilterInfo o) {
        return column.compareTo(o.column);
    }
}
