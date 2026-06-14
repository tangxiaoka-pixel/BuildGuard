package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "platform_user", uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlatformUser extends BaseEntity {
    @Deprecated
    private Long companyId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "platform_user_company", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "company_id", nullable = false)
    @Builder.Default
    private Set<Long> companyIds = new LinkedHashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "platform_user_all_project_company", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "company_id", nullable = false)
    @Builder.Default
    private Set<Long> allProjectCompanyIds = new LinkedHashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "platform_user_project", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "project_id", nullable = false)
    @Builder.Default
    private Set<Long> projectIds = new LinkedHashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "platform_user_company_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    @Builder.Default
    private Set<Long> companyRoleIds = new LinkedHashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "platform_user_project_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    @Builder.Default
    private Set<Long> projectRoleIds = new LinkedHashSet<>();
    private String name;
    @Column(nullable = false) private String phone;
    private String username;
    private String password;
    private String role;
    private String status;
    private LocalDateTime lastLoginTime;
    private String remark;
}
