package com.citizenv.app.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        specialSqlSequenceSelectByProperty.put("ethnicity", "c.ethnicity_id");
        specialSqlSequenceSelectByProperty.put("otherNationality", "c.other_nationality");
        specialSqlSequenceSelectByProperty.put("religion", "c.religion_id");
        specialSqlSequenceSelectByProperty.put("educationalLevel", "c.educational_level");
        specialSqlSequenceSelectByProperty.put("job", "c.job");
        specialSqlSequenceSelectByProperty.put("ageGroup", "CASE when DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),c.date_of_birth)), '%Y') + 0 <= 15 then \"Dưới độ tuổi lao động\" when DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),c.date_of_birth)), '%Y') + 0 <= 65 then \"Trong độ tuổi lao động\" else \"Trên độ tuổi lao động\" end");
        specialSqlSequenceSelectByProperty.put("local", "case when admd2.administrative_unit_id = 7 then 'Nông thôn' else 'Thành thị' end");

        /*specialSqlSequenceJoinProperty.put("bloodType", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("sex", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("maritalStatus", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("ethnicity", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("otherNationality", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("educationalLevel", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("job", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("ageGroup", " join citizens c on. c.id = a.citizen_id");
        specialSqlSequenceJoinProperty.put("local", " join citizens c on. c.id = a.citizen_id");*/
    }

    public CustomAddressRepositoryImpl(EntityManager entityManager) {
        this();
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> countByProperties(Set<String> properties, String code) {
        sb.setLength(0);
        String propertiesSqlSequence = "";
        Iterator<String> propertiesIterator = properties.iterator();
        while (propertiesIterator.hasNext()) {
            sb.append(specialSqlSequenceSelectByProperty.get(propertiesIterator.next()));
            if (propertiesIterator.hasNext()) {
                sb.append(", ");
            }
        }
        propertiesSqlSequence = sb.toString();
        String codeSqlSequence = "";
        sb.setLength(0);
        if (code != null) {
            codeSqlSequence = sb.append(" and admd1.code like '").append(code).append("%' ").toString();
        }

        sb.setLength(0);
        String resultSqlString = "select " + propertiesSqlSequence + ", count(*) from addresses a join citizens c on c.id = a.citizen_id join hamlets h on h.id = a.hamlet_id join administrative_divisions admd1 on admd1.id = h.id join administrative_divisions admd2 on admd2.code = left(admd1.code, 4) where a.address_type = 2 group by " + propertiesSqlSequence;
        resultSqlString = sb.append("select ")
                .append(propertiesSqlSequence)
                .append(", count(*) from addresses a join citizens c on c.id = a.citizen_id join hamlets h on h.id = a.hamlet_id join administrative_divisions admd1 on admd1.id = h.id join administrative_divisions admd2 on admd2.code = left(admd1.code, 4) where a.address_type = 2")
                .append(codeSqlSequence)
                .append("group by ")
                .append(propertiesSqlSequence).toString();
        return entityManager.createNativeQuery(resultSqlString)
                .unwrap(org.hibernate.query.Query.class)
                .getResultList();
    }
}
