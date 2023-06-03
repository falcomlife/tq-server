package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AuthorityDao {

    public List<AuthorityDo> getAll() {
        return Ebean.createQuery(AuthorityDo.class).where().eq("is_delete", false).eq("is_enable", true).findList();
    }

    public List<AuthorityDo> getRoleAuthority(String roleId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" * ");
        sb.append(" from s_authority a ");
        sb.append(" left join r_role_authority ra ");
        sb.append(" on a.id = ra.authority_id ");
        sb.append(" where ra.role_id = :roleId ");
        sb.append(" and a.is_delete = false ");
        sb.append(" and a.is_enable = true ");
        sb.append(" order by a.type asc, a.code asc ");
        return Ebean.createSqlQuery(sb.toString()).setParameter("roleId",roleId).findList().stream().map(item -> {
            AuthorityDo authorityDo = new AuthorityDo();
            authorityDo.setId(item.getString("id"));
            authorityDo.setName(item.getString("name"));
            authorityDo.setType(item.getInteger("type"));
            authorityDo.setCode(item.getString("code"));
            authorityDo.setUser(item.getInteger("user"));
            authorityDo.setIsEnable(item.getBoolean("is_enable"));
            authorityDo.setCreateTime(item.getDate("create_time"));
            authorityDo.setModifiedTime(item.getDate("modified_time"));
            authorityDo.setIsDelete(item.getBoolean("is_delete"));
            return authorityDo;
        }).collect(Collectors.toList());
    }
}
