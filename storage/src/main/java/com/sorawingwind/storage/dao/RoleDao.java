package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RoleDao {

    public RoleDo getById(String id) {
        return Ebean.createQuery(RoleDo.class).where().idEq(id).findOne();
    }

    public List<RoleDo> getByPage(int pageIndex, int pageSize, String name, String companyId) {
        ExpressionList<RoleDo> el = Ebean.createQuery(RoleDo.class).where();
        if (StringUtils.isNotBlank(name)) {
            el.like("name", "%" + name + "%");
        }
        el.eq("company_id", companyId);
        el.eq("is_delete", false);
        el.setFirstRow((pageIndex - 1) * pageSize);
        el.setMaxRows(pageSize);
        List<RoleDo> list = null;
        list = el.order("create_time desc").findList();
        return list;
    }

    public int getCountByPage(String name, String companyId) {
        ExpressionList<RoleDo> el = Ebean.createQuery(RoleDo.class).where();
        if (StringUtils.isNotBlank(name)) {
            el.like("name", "%" + name + "%");
        }
        el.eq("company_id", companyId);
        el.eq("is_delete", false);
        return el.findCount();
    }

    public void save(RoleDo doo) {
        Ebean.save(doo);
    }

    public void update(RoleDo doo) {
        Ebean.update(doo);
    }

    public void updateAll(List<RoleDo> list) {
        Ebean.updateAll(list);
    }

    public void saveAuthority(String id, String authorityId) {
        Ebean.createSqlUpdate("insert into r_role_authority (role_id,authority_id) values (:roleId,:authorityId)").setParameter("roleId", id).setParameter("authorityId", authorityId).execute();
    }


    public List<RoleDo> getAll() {
        return Ebean.createQuery(RoleDo.class).where().eq("is_delete", false).findList();
    }

    public List<RoleDo> getRoleAuthority(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" * ");
        sb.append(" from s_role r ");
        sb.append(" left join r_user_role ru ");
        sb.append(" on r.id = ru.role_id ");
        sb.append(" where ru.user_id = :userId ");
        sb.append(" and r.is_delete = false ");
        return Ebean.createSqlQuery(sb.toString()).setParameter("userId", userId).findList().stream().map(item -> {
            RoleDo roleDo = new RoleDo();
            roleDo.setId(item.getString("id"));
            roleDo.setName(item.getString("name"));
            roleDo.setCompanyId(item.getString("company_id"));
            roleDo.setCreateTime(item.getDate("create_time"));
            roleDo.setModifiedTime(item.getDate("modified_time"));
            roleDo.setIsDelete(item.getBoolean("is_delete"));
            return roleDo;
        }).collect(Collectors.toList());
    }

    public void deleteAuthority(String id, String authorityId) {
        Ebean.createSqlUpdate("delete from r_role_authority where role_id = :roleId and authority_id = :authorityId").setParameter("roleId", id).setParameter("authorityId", authorityId).execute();
    }
}
