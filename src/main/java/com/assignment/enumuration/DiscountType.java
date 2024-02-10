package com.assignment.enumuration;

public enum DiscountType {

    DISCOUNT("DISCOUNT"),
    TAX("TAX");

    private final String value;
    DiscountType( String value){
        this.value=value;
    };

    public String getValue(){
        return value;
    }

}
