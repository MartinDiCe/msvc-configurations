package com.diceprojects.msvcconfigurations.persistences.models.dtos;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Clase DTO para representar los datos del usuario.
 */
@Data
public class UserDTO {

    private String id;
    private String username;
    private String password;
    private String status;
    private boolean deleted;
    private Date deleteDate;
    private Date createDate;
    private Date updateDate;
    private String securityToken;
    private boolean forcePasswordChange;
    private Set<String> roleIds;

    public UserDTO(String id, String username, String password, String status, boolean deleted,
                   Date deleteDate, Date createDate, Date updateDate, String securityToken,
                   boolean forcePasswordChange, Set<String> roleIds) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.deleted = deleted;
        this.deleteDate = deleteDate;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.securityToken = securityToken;
        this.forcePasswordChange = forcePasswordChange;
        this.roleIds = roleIds;
    }

    public UserDTO() {}
}
