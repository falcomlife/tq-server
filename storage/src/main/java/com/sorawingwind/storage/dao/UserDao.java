package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.ao.storage.UserAo;
import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.cotte.estate.bean.pojo.doo.storage.UserDo;
import com.cotte.estate.bean.pojo.dto.UserAuthenticationDto;
import io.ebean.Ebean;
import io.ebean.SqlRow;
import io.ebean.Update;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDao {

    public UserAuthenticationDto loginAuthentication(UserAuthenticationDto dto) {
        CompanyDo company = Ebean.createQuery(CompanyDo.class).where().eq("code", dto.getCompanyCode()).eq("is_delete", false).findOne();
        if (company == null) {
            return null;
        }
        UserDo user = Ebean.createQuery(UserDo.class).where().eq("account", dto.getAccount()).eq("company_id", company.getId()).eq("is_delete", false).eq("is_lock", false).findOne();
        if (user == null) {
            return null;
        }
        List<RoleDo> roleDos = Ebean.createSqlQuery(" select * from r_user_role r left join s_role s on r.role_id = s.id where r.user_id = :userId and s.is_delete = false").setParameter("userId", user.getId()).findList().stream().map(item -> {
            RoleDo roleDo = new RoleDo();
            roleDo.setId(item.getString("id"));
            roleDo.setName(item.getString("name"));
            roleDo.setIsDelete(item.getBoolean("is_delete"));
            roleDo.setCreateTime(item.getDate("create_time"));
            roleDo.setModifiedTime(item.getDate("modified_time"));
            return roleDo;
        }).collect(Collectors.toList());
        List<AuthorityDo> authorityDosSum = new ArrayList<>();
        roleDos.stream().forEach(role -> {
            List<AuthorityDo> authorityDos = Ebean.createSqlQuery(" select * from r_role_authority r left join s_authority s on r.authority_id = s.id where r.role_id = :roleId and s.is_delete = false and s.is_enable = true").setParameter("roleId",role.getId()).findList().stream().map(item -> {
                AuthorityDo authorityDo = new AuthorityDo();
                authorityDo.setId(item.getString("id"));
                authorityDo.setName(item.getString("name"));
                authorityDo.setCode(item.getString("code"));
                authorityDo.setType(item.getInteger("type"));
                authorityDo.setIsEnable(item.getBoolean("is_enable"));
                authorityDo.setCreateTime(item.getDate("create_time"));
                authorityDo.setModifiedTime(item.getDate("modified_time"));
                authorityDo.setIsDelete(item.getBoolean("is_delete"));
                return authorityDo;
            }).collect(Collectors.toList());
            authorityDosSum.addAll(authorityDos);
        });
        return new UserAuthenticationDto(user.getId(), company.getName(), company.getCode(), user.getAccount(), user.getName(), user.getPassword(), roleDos, authorityDosSum);
    }

    public void save(UserDo userDo) {
        Ebean.save(userDo);
    }

    public UserDo getByAccount(String account) {
        return Ebean.createQuery(UserDo.class).where().eq("account",account).findOne();
    }

    public void rebackPassword(UserDo userDo) {
        String updStatement = "update s_user set password = :password where id = :id";
        Update<UserDo> update = Ebean.createUpdate(UserDo.class, updStatement);
        update.set("password", userDo.getPassword());
        update.set("id", userDo.getId());
        update.execute();
    }
}
