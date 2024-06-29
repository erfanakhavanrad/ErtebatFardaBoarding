package com.example.ertebatfardaboarding.security;

import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
//@Lazy
public class CustomPermissionEvaluator implements PermissionEvaluator {
    DataSource dataSource;

    Connection connection;

    @Autowired
    RoleRepository roleRepo;


    public CustomPermissionEvaluator(DataSource ds) {
        try {
            dataSource = ds;
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
    }


    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(),
                permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities())
            if (isAuthorized(grantedAuth.toString(), targetType, permission))
                return true;

        return false;
    }

    private boolean isAuthorized(String privilage, String domainName, String permission) {
        try {
            if (privilage.substring(0, privilage.indexOf(",")).equalsIgnoreCase(domainName) &&
                    privilage.substring(privilage.indexOf(",") + 1, privilage.length()).equalsIgnoreCase(permission))
                return true;

            return false;
        } catch (Exception e) {
            return false;
        }
    }


    private List<Privilege> getDataFromPrepareStatement(int id) {
        try {
            List<Privilege> privileges = new ArrayList<>();

            PreparedStatement pstmt = connection.
                    prepareStatement("SELECT * FROM audiobook.role_privileges as rpv join audiobook.Privileges prv on rpv.privilege_id = prv.id where role_id=?");
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                privileges.add(new Privilege(rs.getString("access"), rs.getString("domain")));
            }
            rs.close();
            pstmt.close();


            return privileges;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Integer> getRoleIdsDataFromPrepareStatement(String userName) {
        try {
            List<Integer> roles = new ArrayList<>();
            if (connection.isClosed())
                connection = dataSource.getConnection();
            PreparedStatement pstmt = connection.
                    prepareStatement("select usr_role.role_id as id from audiobook.users_role as usr_role join audiobook.Users as usr on usr.id = usr_role.user_id where usr.userName=?");
            pstmt.setString(1, userName);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getInt("id"));
            }
            rs.close();
//            pstmt.close();

            return roles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Privilege> fetchPrivileges(int roleId) {
        return getDataFromPrepareStatement(roleId);
    }

}
