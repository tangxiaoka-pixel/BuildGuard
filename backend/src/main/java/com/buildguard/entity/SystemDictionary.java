package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_dictionary", uniqueConstraints = @UniqueConstraint(name = "uk_system_dictionary_type_code", columnNames = {"dictType", "code"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SystemDictionary extends BaseEntity {
    @Column(nullable = false) private String dictType;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String name;
    private Integer sortOrder;
    private String status;
    private String remark;
}
