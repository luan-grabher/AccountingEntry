package lctocontabil.Model;

import java.math.BigDecimal;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import lctocontabil.Entity.ContabilityEntry;

public class Filter {

    public static Predicate<ContabilityEntry> notConciliated() {
        return l -> !l.isConciliated();
    }

    public static Predicate<ContabilityEntry> conciliated() {
        return l -> l.isConciliated();
    }

    public static Predicate<ContabilityEntry> valueEqualTo(BigDecimal big) {
        return l -> l.getValue().compareTo(big) == 0;
    }

    public static Predicate<ContabilityEntry> valueSmallerThan(BigDecimal big) {
        return l -> l.getValue().compareTo(big) == -1;
    }

    public static Predicate<ContabilityEntry> valueMultipleOf(BigDecimal big) {
        return l -> big.remainder(l.getValue()).compareTo(BigDecimal.ZERO) == 0;
    }

    public static Predicate<ContabilityEntry> keyEqualsTo(Integer chave) {
        return l -> Objects.equals(l.getKey(), chave);
    }

    public static Predicate<ContabilityEntry> existsOnMap(Map<Integer, ContabilityEntry> entries) {
        return l -> entries.get(l.getKey()) != null;
    }

    public static Predicate<ContabilityEntry> notExistsOnMap(Map<Integer, ContabilityEntry> entries) {
        return existsOnMap(entries).negate();
    }

    public static Predicate<ContabilityEntry> accountDebitOrCreditEqualsTo(Integer account) {
        return accountCreditEqualsTo(account).or(accountCreditEqualsTo(account));
    }

    public static Predicate<ContabilityEntry> accountCreditEqualsTo(Integer account) {
        return l -> Objects.equals(l.getAccountCredit(), account);
    }

    public static Predicate<ContabilityEntry> accountDebitEqualsTo(Integer account) {
        return l -> Objects.equals(l.getAccountDebit(), account);
    }

    public static Predicate<ContabilityEntry> participantDebitOrCreditEqualsTo(Integer participant) {
        return participantCreditEqualsTo(participant).or(participantDebitEqualsTo(participant));
    }

    public static Predicate<ContabilityEntry> participantCreditEqualsTo(Integer participant) {
        return l -> Objects.equals(l.getParticipantCredit(), participant);
    }

    public static Predicate<ContabilityEntry> participantDebitEqualsTo(Integer participant) {
        return l -> Objects.equals(l.getParticipantDebit(), participant);
    }

    public static Predicate<ContabilityEntry> documentEqualsTo(String document) {
        return l -> l.getDocument().equals(document);
    }
}
