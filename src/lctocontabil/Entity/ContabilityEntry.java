package lctocontabil.Entity;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.regex.Pattern;

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

    public ContabilityEntry getObject() {
        return this;
    }

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
        if(complement.length()>200){
            return complement.substring(0, 201);
        }else{
            return complement;
        }
    }

    public void setComplement(String complement) {
        this.complement = normalizeString(complement);
    }

    public String getDocument() {
        if(complement.length()>15){
            return document.substring(0, 16);
        }else{
            return document;
        }
    }

    public void setDocument(String document) {
        document = normalizeString(document);
        try {
            this.document = Long.valueOf(document).toString();
        } catch (Exception e) {
            this.document = document;
        }                
    }

    public String normalizeString(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(nfdNormalizedString).replaceAll("");
        str = str.replaceAll("\\$", "S");
        str = str.replaceAll("[^a-zA-Z0-9 \\/\\\\]+", " ");
        str = str.replaceAll(" +", " ");
        return str;
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
     *
     * @return Retorna se está conciliado no débito ou no crédito
     */
    public Boolean isConciliated() {
        return conciliedCredit || conciliedDebit;
    }

    /**
     * Retorna se o lançamento tem o participante no debito ou credito
     *
     * @param participant Codigo participante
     */
    public Boolean isParticipant(Integer participant) {
        return participant.equals(participantCredit) || participant.equals(participantDebit);
    }
}
