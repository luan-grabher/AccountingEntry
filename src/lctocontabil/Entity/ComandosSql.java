package lctocontabil.Entity;

import Auxiliar.Parametros;
import java.util.ArrayList;
import java.util.List;
import sql.Entity.CampoSQL;
import sql.SQL;

public class ComandosSql {

    private static final int CONTA_TIPO_PARTICIPANTE = 1;
    private static final int CONTA_TIPO_CONTACTB = 2;

    public Parametros t_Lancamentos = new Parametros("");

    public String whereLctos(Integer codEmpresa, String dataInicio, String dataFinal, Integer contaCTB, String filtroExtra) {
        String sql = " " + t_Lancamentos.get("col_empresa").getString() + " = " + codEmpresa + " and ";
        sql += " " + t_Lancamentos.get("col_data").getString() + " between '" + dataInicio + "' AND '" + dataFinal + "' ";
        sql += " AND (" + t_Lancamentos.get("col_debito").getString() + " = " + contaCTB + " OR " + t_Lancamentos.get("col_credito").getString() + " = " + contaCTB + ") ";
        sql += (filtroExtra.equals("") ? "" : " and " + filtroExtra);

        return sql;
    }

    public String whereLctos(Integer codEmpresa, String filtro) {
        String sql = " " + t_Lancamentos.get("col_empresa").getString() + " = " + codEmpresa;
        sql += (filtro.equals("") ? "" : " and " + filtro);

        return sql;
    }

    public String whereChave(Integer codEmpresa, Long chaveLcto) {
        return whereLctos(codEmpresa, t_Lancamentos.get("col_chave").getString() + " = " + chaveLcto);
    }

    public String updateLcto(ContabilityEntry lcto) {
        List<CampoSQL> campos = new ArrayList<>();
        campos.add(new CampoSQL(t_Lancamentos.get("col_data").getString(), "=", lcto.getData().getString(), true, false));
        campos.add(new CampoSQL(t_Lancamentos.get("col_planoPadrao").getString(), "=", lcto.getPlanoPadrao().toString(), true, false));
        campos.add(new CampoSQL(t_Lancamentos.get("col_debito").getString(), "=", lcto.getDeb().toString(), true, true));
        campos.add(new CampoSQL(t_Lancamentos.get("col_credito").getString(), "=", lcto.getCred().toString(), true, true));
        campos.add(new CampoSQL(t_Lancamentos.get("col_participanteDebito").getString(), "=", lcto.getParticipantDebit().toString(), true, true));
        campos.add(new CampoSQL(t_Lancamentos.get("col_participanteCredito").getString(), "=", lcto.getParticipantCredit().toString(), true, true));
        campos.add(new CampoSQL(t_Lancamentos.get("col_historicoPadrao").getString(), "=", lcto.getHistoricoPadrao().toString(), true, true));
        campos.add(new CampoSQL(t_Lancamentos.get("col_complementoHistorico").getString(), "=", lcto.getComplemento().getString(), true, false));
        campos.add(new CampoSQL(t_Lancamentos.get("col_documento").getString(), "=", lcto.getDocumento().getString(), true, false));
        campos.add(new CampoSQL(t_Lancamentos.get("col_valor").getString(), "=", lcto.getValor().getString(), true, false));
        campos.add(new CampoSQL(t_Lancamentos.get("col_conciliadoDebito").getString(), "=", lcto.getConciliadoDeb(), true, false));
        campos.add(new CampoSQL(t_Lancamentos.get("col_conciliadoCredito").getString(), "=", lcto.getConciliadoCred(), true, false));

        String setCommand = " SET " + CampoSQL.imprimirCampos(campos, " , ");

        List<CampoSQL> camposWhere = new ArrayList<>();
        camposWhere.add(new CampoSQL(t_Lancamentos.get("col_chave").getString(), "=", lcto.getKey().toString(), false, false));
        camposWhere.add(new CampoSQL(t_Lancamentos.get("col_empresa").getString(), "=", lcto.getCodigoEmpresa().toString(), false, false));
        String whereCommand = " where " + CampoSQL.imprimirCampos(camposWhere, " AND ");

        String command = "UPDATE " + t_Lancamentos.get("nome").getString() + " " + setCommand + whereCommand;

        return command;
    }

    public String selectLctos(String filtro) {
        String where = filtro.equals("") ? "" : " where " + filtro;

        StringBuilder sql = new StringBuilder("select ");
        sql.append(t_Lancamentos.get("col_chave").getString()).append(" CHAVE, ");
        sql.append(t_Lancamentos.get("col_empresa").getString()).append(" EMPRESA, ");
        sql.append(t_Lancamentos.get("col_filial").getString()).append(" FILIAL, ");
        sql.append(t_Lancamentos.get("col_data").getString()).append(" DATA, ");
        sql.append(t_Lancamentos.get("col_planoPadrao").getString()).append(" PLANOPADRAO, ");
        sql.append(t_Lancamentos.get("col_debito").getString()).append(" DEBITO, ");
        sql.append(t_Lancamentos.get("col_credito").getString()).append(" CREDITO, ");
        sql.append(t_Lancamentos.get("col_participanteDebito").getString()).append(" PARTICIPANTEDEBITO, ");
        sql.append(t_Lancamentos.get("col_participanteCredito").getString()).append(" PARTICIPANTECREDITO, ");
        sql.append(t_Lancamentos.get("col_historicoPadrao").getString()).append(" HISTORICOPADRAO, ");
        sql.append(t_Lancamentos.get("col_complementoHistorico").getString()).append(" COMPLEMENTOHISTORICO, ");
        sql.append(t_Lancamentos.get("col_documento").getString()).append(" DOCUMENTO, ");
        sql.append(t_Lancamentos.get("col_valor").getString()).append(" VALOR, ");
        sql.append(t_Lancamentos.get("col_conciliadoDebito").getString()).append(" CONCILIADODEBITO, ");
        sql.append(t_Lancamentos.get("col_conciliadoCredito").getString()).append(" CONCILIADOCREDITO ");
        sql.append(" FROM ").append(t_Lancamentos.get("nome").getString()).append(" ");
        sql.append(where);

        return sql.toString();
    }

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

    public String selectChavesAnterioresAData(Integer codigoEmpresa, Integer conta, String sqlDate, int contaTipo) {
        String colunaDebito;
        String colunaCredito;
        if (contaTipo == CONTA_TIPO_PARTICIPANTE) {
            colunaDebito = t_Lancamentos.get("col_participanteDebito").getString();
            colunaCredito = t_Lancamentos.get("col_participanteCredito").getString();
        } else {
            colunaDebito = t_Lancamentos.get("col_debito").getString();
            colunaCredito = t_Lancamentos.get("col_credito").getString();
        }

        String sql = "select LIST(" + t_Lancamentos.get("col_chave").getString() + ") from " + t_Lancamentos.get("nome").getString() + " l where "
                + " l." + t_Lancamentos.get("col_empresa").getString() + " = " + codigoEmpresa + " and "
                + " (l." + colunaCredito + " = " + conta + " or l." + colunaDebito + " = " + conta + ") and "
                + " L." + t_Lancamentos.get("col_data").getString() + " < '" + sqlDate + "'";
        return sql;
    }

    public List<String> conciliarChaves(Integer codigoEmpresa, String chavesSeparadasPorVirgula, String conciliacao) {
        try {
            if (!chavesSeparadasPorVirgula.equals("")) {
                
                //Cria string paadrao para adicionar as listas de IN
                StringBuilder sqlPadrao = new StringBuilder();
                sqlPadrao.append("UPDATE ").append(t_Lancamentos.get("nome").getString()).append(" SET ");
                sqlPadrao.append(t_Lancamentos.get("col_conciliadoDebito").getString()).append(" = ").append("'" + conciliacao + "'");
                sqlPadrao.append(" , ");
                sqlPadrao.append(t_Lancamentos.get("col_conciliadoCredito").getString()).append(" = ").append("'" + conciliacao + "'");
                sqlPadrao.append(" where ");
                sqlPadrao.append(t_Lancamentos.get("col_empresa").getString()).append(" = ").append(codigoEmpresa);
                sqlPadrao.append(" AND ");
                
                String sqlPadraoStr  = sqlPadrao.toString();
                
                List<String> listasExpressaoIn = SQL.getListInWithMaxSplit(chavesSeparadasPorVirgula, t_Lancamentos.get("col_chave").getString());

                for (int i = 0; i < listasExpressaoIn.size(); i++){
                    listasExpressaoIn.set(i, sqlPadraoStr + listasExpressaoIn.get(i));
                }

                return listasExpressaoIn;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
