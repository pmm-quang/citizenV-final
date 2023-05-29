package com.citizenv.app.repository;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;


public class CustomAddressRepositoryImpl implements CustomAddressRepository {

    @PersistenceContext
    private EntityManager entityManager;

    StringBuilder sb;

    Map<String, String> specialSqlSequenceSelectByProperty;
    Map<String, String> specialSqlSequenceJoinProperty;

    public CustomAddressRepositoryImpl() {
        sb = new StringBuilder();
        specialSqlSequenceSelectByProperty = new HashMap<>();
        specialSqlSequenceJoinProperty = new HashMap<>();
        specialSqlSequenceSelectByProperty.put("age", "DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),c.date_of_birth)), '%Y') + 0");
        specialSqlSequenceSelectByProperty.put("bloodType", "c.blood_type");
        specialSqlSequenceSelectByProperty.put("sex", "c.sex");
        specialSqlSequenceSelectByProperty.put("maritalStatus", "c.marital_status");
        specialSqlSequenceSelectByProperty.put("ethnicity", "eth.name");
        specialSqlSequenceSelectByProperty.put("otherNationality", "c.other_nationality");
        specialSqlSequenceSelectByProperty.put("religion", "IFNULL(re.name, 'Không')");
        specialSqlSequenceSelectByProperty.put("educationalLevel", "c.educational_level");
        specialSqlSequenceSelectByProperty.put("job", "c.job");
        specialSqlSequenceSelectByProperty.put("ageGroup", "CASE when DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),c.date_of_birth)), '%Y') + 0 <= 15 then \"Dưới độ tuổi lao động\" when DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),c.date_of_birth)), '%Y') + 0 <= 65 then \"Trong độ tuổi lao động\" else \"Trên độ tuổi lao động\" end");
        specialSqlSequenceSelectByProperty.put("local", "case when admd2.administrative_unit_id = 7 then 'Nông thôn' else 'Thành thị' end");
    }

    public CustomAddressRepositoryImpl(EntityManager entityManager) {
        this();
        this.entityManager = entityManager;
    }

    @Override
    public List<Map<String, Object>> countByProperties(Set<String> properties, String code) {
        sb.setLength(0);
        String propertiesSqlSelectSequence;
        String propertiesGroupBySequence = "";
        Iterator<String> propertiesIterator = properties.iterator();
        while (propertiesIterator.hasNext()) {
            String tempKey = propertiesIterator.next();
            String temp = specialSqlSequenceSelectByProperty.get(tempKey);
            sb.append(temp);
            sb.append(" as ");
            sb.append(tempKey);
            propertiesGroupBySequence += tempKey;
            if (propertiesIterator.hasNext()) {
                sb.append(", ");
                propertiesGroupBySequence += ", ";
            }
        }
        propertiesSqlSelectSequence = sb.toString();
        String codeSqlSequence = "";
        sb.setLength(0);
        if (code != null) {
            codeSqlSequence = sb.append(" and admd1.code like '").append(code).append("%' ").toString();
        }

        sb.setLength(0);
        String resultSqlString;
        resultSqlString = sb.append("select ")
                .append(propertiesSqlSelectSequence)
                .append(", count(*) as population from addresses a join citizens c on c.id = a.citizen_id join hamlets h on h.id = a.hamlet_id join administrative_divisions admd1 on admd1.id = h.id join administrative_divisions admd2 on admd2.code = left(admd1.code, 4) join ethnicities eth on eth.id = c.ethnicity_id left join religions re on re.id = c.religion_id where a.address_type = 2")
                .append(codeSqlSequence)
                .append("group by ")
                .append(propertiesGroupBySequence).toString();
        Query query = entityManager.createNativeQuery(resultSqlString);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String,Object>> result = nativeQuery.getResultList();
        return result;
    }
}
