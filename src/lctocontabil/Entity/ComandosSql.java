package lctocontabil.Entity;

import java.util.List;
import sql.SQL;

public class ComandosSql {

    private static final int CONTA_TIPO_PARTICIPANTE = 1;
    private static final int CONTA_TIPO_CONTACTB = 2;

    public Parametros t_Lancamentos = new Parametros("");

    public String selectSaldoConta(Integer codigoEmpresa, Integer conta, String sqlDate, String colunaDebito, String colunaCredito) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select SUM(L.").append(t_Lancamentos.get("col_valor").getString()).append(") ");
        sql.append(" from ").append(t_Lancamentos.get("nome").getString()).append(" L ");
        sql.append(" where ").append(" l.").append(t_Lancamentos.get("col_empresa").getString()).append(" = ").append(codigoEmpresa);
        sql.append(" and  l.").append(t_Lancamentos.get("col_data").getString()).append(" <= '").append(sqlDate).append("' ");
        sql.append(" AND (l.").append(colunaDebito).append(" = ").append(conta).append(") ");
        sql.append(" AND IIF(").append(t_Lancamentos.get("col_conciliadoCredito").getString()).append(" IS NULL,'FALSE', ");
        sql.append(t_Lancamentos.get("col_conciliadoCredito").getString()).append(") <> 'TRUE'");
        sql.append(" AND IIF (").append(t_Lancamentos.get("col_conciliadoDebito").getString()).append(" IS NULL,'FALSE', ");
        sql.append(t_Lancamentos.get("col_conciliadoDebito").getString()).append(") <> 'TRUE' ");
        sql.append(" union ");
        sql.append(" select SUM(L.").append(t_Lancamentos.get("col_valor").getString()).append(") ");
        sql.append(" from ").append(t_Lancamentos.get("nome").getString()).append(" L ");
        sql.append(" where ");
        sql.append(" l.").append(t_Lancamentos.get("col_empresa").getString()).append(" = ").append(codigoEmpresa);
        sql.append(" and  l.").append(t_Lancamentos.get("col_data").getString()).append(" <= '").append(sqlDate + "' ");
        sql.append(" AND (l.").append(colunaCredito).append(" = ").append(conta).append(") ");
        sql.append(" AND IIF(").append(t_Lancamentos.get("col_conciliadoCredito").getString()).append(" IS NULL,'FALSE', ");
        sql.append(t_Lancamentos.get("col_conciliadoCredito").getString()).append(") <> 'TRUE'");
        sql.append(" AND IIF (").append(t_Lancamentos.get("col_conciliadoDebito").getString()).append(" IS NULL,'FALSE', ");
        sql.append(t_Lancamentos.get("col_conciliadoDebito").getString()).append(") <> 'TRUE' ");

        return sql.toString();
    }
}
