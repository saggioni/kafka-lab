package br.com.saggioni.lab.domain;

public class Proposal {
    private String id;
    private String conditions;
    private double value;

    public Proposal() {   }

    public Proposal(String id, String conditions, double value) {
        this.id = id;
        this.conditions = conditions;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getConditions() {
        return conditions;
    }

    public double getValue() {
        return value;
    }
}
