package lctocontabil.Entity;

import java.math.BigDecimal;
import java.util.Calendar;

public class ContabilityEntry {

    private Integer key = null;
    private Integer enterprise = null;

    private Calendar date = null;
    private Integer defaultPlan = null;
    private Integer accountDebit = null;
    private Integer accountCredit = null;
    private Integer participantDebit = null;
    private Integer participantCredit = null;
    private Integer historyCode = null;
    private String complement = "";
    private String document = null;
    private BigDecimal value = new BigDecimal("0.00");

    private Boolean conciliedDebit = false;
    private Boolean conciliedCredit = false;

    public Integer getKey() {
        return key;
    }

    /**
     * Define a chave, se a chave já estiver setada, permanece a mesma de antes
     *
     * @param key A chave do lançamento
     */
    public void setKey(Integer key) {
        if (this.key == null) {
            this.key = key;
        }
    }

    public Integer getEnterprise() {
        return enterprise;
    }

    /**
     * Define a empresa, se a empresa já estiver setada, permanece a mesma de
     * antes.
     *
     * @param enterprise o número da empresa
     */
    public void setEnterprise(Integer enterprise) {
        if (this.enterprise == null) {
            this.enterprise = enterprise;
        }
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Integer getDefaultPlan() {
        return defaultPlan;
    }

    public void setDefaultPlan(Integer defaultPlan) {
        this.defaultPlan = defaultPlan;
    }

    public Integer getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(Integer accountDebit) {
        this.accountDebit = accountDebit;
    }

    public Integer getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(Integer accountCredit) {
        this.accountCredit = accountCredit;
    }

    public Integer getParticipantDebit() {
        return participantDebit;
    }

    public void setParticipantDebit(Integer participantDebit) {
        this.participantDebit = participantDebit;
    }

    public Integer getParticipantCredit() {
        return participantCredit;
    }

    public void setParticipantCredit(Integer participantCredit) {
        this.participantCredit = participantCredit;
    }

    public Integer getHistoryCode() {
        return historyCode;
    }

    public void setHistoryCode(Integer historyCode) {
        this.historyCode = historyCode;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getConciliedDebit() {
        return conciliedDebit;
    }

    public void setConciliedDebit(Boolean conciliedDebit) {
        this.conciliedDebit = conciliedDebit;
    }

    public Boolean getConciliedCredit() {
        return conciliedCredit;
    }

    public void setConciliedCredit(Boolean conciliedCredit) {
        this.conciliedCredit = conciliedCredit;
    }

    /**
     * Define conciliado crédito e débito como TRUE
     */
    public void conciliate() {
        conciliedCredit = true;
        conciliedDebit = true;
    }

    /**
     * Define conciliado crédito e débito como FALSE
     */
    public void disconciliate() {
        conciliedCredit = false;
        conciliedDebit = false;
    }

    /**
     * Retorna se está conciliado no débito ou no crédito
     * @return Retorna se está conciliado no débito ou no crédito
     */
    public Boolean isConciliated() {
        return conciliedCredit || conciliedDebit;
    }
}
