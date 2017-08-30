package assignment.solution;

import java.time.LocalDate;

public class TradeEntity {
    private String entityName;
    private String buySell;
    private Double agreedFx;
    private String currency;
    private LocalDate instructionDate;
    private LocalDate settlementDate;
    private LocalDate effectiveSettlementDate;
    private Integer units;
    private Double pricePerUnit;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public Double getAgreedFx() {
        return agreedFx;
    }

    public void setAgreedFx(Double agreedFx) {
        this.agreedFx = agreedFx;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getInstructionDate() {
        return instructionDate;
    }

    public void setInstructionDate(LocalDate instructionDate) {
        this.instructionDate = instructionDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public LocalDate getEffectiveSettlementDate() {
        return effectiveSettlementDate;
    }

    public void setEffectiveSettlementDate(LocalDate effectiveSettlementDate) {
        this.effectiveSettlementDate = effectiveSettlementDate;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entityName='" + entityName + '\'' +
                ", buySell='" + buySell + '\'' +
                ", agreedFx=" + agreedFx +
                ", currency='" + currency + '\'' +
                ", instructionDate=" + instructionDate +
                ", settlementDate=" + settlementDate +
                ", DoW1 =" + settlementDate.getDayOfWeek() +
                ", effectiveSettlementDate=" + effectiveSettlementDate +
                ", DoW2 =" + effectiveSettlementDate.getDayOfWeek() +
                ", units=" + units +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
