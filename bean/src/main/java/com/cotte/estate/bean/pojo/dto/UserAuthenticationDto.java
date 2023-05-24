package com.cotte.estate.bean.pojo.dto;

import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import io.ebeaninternal.server.lib.util.Str;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserAuthenticationDto {

    private String id;
    private String companyName;
    private String companyCode;
    private String username;

    private String account;
    private String password;
    private List<RoleDo> roles;
    private List<AuthorityDo> authoritys;

    public UserAuthenticationDto(String id,String companyName, String companyCode, String account,String username, String password, List<RoleDo> roles, List<AuthorityDo> authoritys) {
        this.id = id;
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.username = username;
        this.account = account;
        this.password = password;
        this.roles = roles;
        this.authoritys = authoritys;
    }
}
