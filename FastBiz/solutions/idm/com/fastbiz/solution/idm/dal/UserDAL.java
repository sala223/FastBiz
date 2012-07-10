package com.fastbiz.solution.idm.dal;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import com.fastbiz.core.dal.EclipseLinkDataAccessFoundation;
import com.fastbiz.solution.idm.entity.Constants;
import com.fastbiz.solution.idm.entity.User;

@Repository(value = "userDAL")
public class UserDAL extends EclipseLinkDataAccessFoundation implements Constants{

    public User getUserById(long id){
        return this.find(User.class, id);
    }

    public User getUserByCode(String code){
        CriteriaBuilder builder = createQueryBuilder(User.class);
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(builder.equal(root.get(USER.USER_NAME_PROP), code));
        return executeSingleQuery(query);
    }

    public int updateUserPassword(String name, String newEncodedPassword){
        String fmt = "UPDATE %s u set u.%s=:PASSWORD WHERE u.%s=:UNAME";
        String sql = String.format(fmt, USER.USER_ENTITY_NAME, USER.USER_PASSWORD_PROP, USER.USER_NAME_PROP);
        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("PASSWORD", newEncodedPassword);
        query.setParameter("UNAME", name);
        return query.executeUpdate();
    }
    
    public void createUser(User user){
         super.insert(user);
    }
}
