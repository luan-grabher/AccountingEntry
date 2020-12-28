SELECT
LIST(BDCHAVE)
FROM VSUC_EMPRESAS_TLAN
WHERE
BDCODEMP = :enterprise
AND BDDATA < ':date'
AND (BDDEBITO = :account OR BDCREDITO = :account)
AND CAST(L.BDCONCC AS VARCHAR(10)) :conciled 'TRUE' AND CAST(L.BDCONCD AS VARCHAR(10)) :conciled 'TRUE'
AND (:participant is NULL OR (:participant is not NULL AND (BDCODTERCEIROC = :participant OR BDCODTERCEIROD = :participant)))
