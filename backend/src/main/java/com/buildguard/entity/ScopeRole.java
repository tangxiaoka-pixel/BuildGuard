package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "scope_role")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScopeRole extends BaseEntity {
    private String scopeType;
    private Long scopeId;
    private String name;
    private String status;
    private Boolean systemRole;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "scope_role_module", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "module_key")
    @Builder.Default
    private Set<String> moduleKeys = new LinkedHashSet<>();
}
