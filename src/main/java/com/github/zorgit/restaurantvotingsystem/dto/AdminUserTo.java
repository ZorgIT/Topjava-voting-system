package com.github.zorgit.restaurantvotingsystem.dto;

import com.github.zorgit.restaurantvotingsystem.HasIdAndEmail;
import com.github.zorgit.restaurantvotingsystem.model.Role;
import com.github.zorgit.restaurantvotingsystem.util.validation.NoHtml;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Date;
import java.util.Set;


@Value
@EqualsAndHashCode(callSuper = true)
public class AdminUserTo extends NamedTo implements HasIdAndEmail {

    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    boolean enabled;

    Date registered = new Date();
    Set<Role> roles;

    public AdminUserTo(Integer id,
                       String name,
                       String email,
                       String password,
                       Boolean enabled,
                       Set<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserTo:" + id + '[' + email + ']';
    }

}
