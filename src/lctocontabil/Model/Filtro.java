package lctocontabil.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import lctocontabil.Entity.LctoContabil;

public class Filtro {

    public static Predicate<LctoContabil> naoConciliado() {
        return l -> !l.isConciliado();
    }
    
    public static Predicate<LctoContabil> conciliado() {
        return l -> l.isConciliado();
    }
    
    public static Predicate<LctoContabil> valorIgual(BigDecimal big){
        return l -> l.getValor().getBigDecimal().compareTo(big) == 0;
    }
    
    public static Predicate<LctoContabil> valorMenorQue(BigDecimal big){
        return l -> l.getValor().getBigDecimal().compareTo(big) == -1;
    }
    
    public static Predicate<LctoContabil> multiploDe(BigDecimal big){
        return l -> big.remainder(l.getValor().getBigDecimal()).compareTo(BigDecimal.ZERO) == 0;
    }
    
    public static Predicate<LctoContabil> chave(Integer chave){
        return l -> Objects.equals(l.getChave(), chave);
    }
    
    public static Predicate<LctoContabil> estaNaLista(List<LctoContabil> lista){
        return l -> lista.stream().anyMatch(chave(l.getChave()));
    }
    
    public static Predicate<LctoContabil> naoEstaNaLista(List<LctoContabil> lista){
        return estaNaLista(lista).negate();
    }
    
    public static Predicate<LctoContabil> conta(Integer contaCtb) {
        return contaDebito(contaCtb).or(contaCredito(contaCtb));
    }
    
    public static Predicate<LctoContabil> contaDebito(Integer contaCtb) {
        return l -> Objects.equals(l.getDeb(), contaCtb);
    }
    
    public static Predicate<LctoContabil> contaCredito(Integer contaCtb) {
        return l -> Objects.equals(l.getCred(), contaCtb);
    }

    public static Predicate<LctoContabil> participante(Integer participante) {
        return participante_Credito(participante).or(participante_Debito(participante));
    }

    public static Predicate<LctoContabil> participante_Credito(Integer participante) {
        return l -> Objects.equals(l.getTerceiroCred(), participante);
    }

    public static Predicate<LctoContabil> participante_Debito(Integer participante) {
        return l -> Objects.equals(l.getTerceiroDeb(), participante);
    }

    public static Predicate<LctoContabil> documento(String documento) {
        return l -> l.getDocumento().getString().equals(documento);
    }
}
