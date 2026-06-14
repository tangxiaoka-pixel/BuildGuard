package com.buildguard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "work_type_dictionary", uniqueConstraints = {
        @UniqueConstraint(name = "uk_work_type_code", columnNames = "code"),
        @UniqueConstraint(name = "uk_work_type_name", columnNames = "name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkTypeDictionary extends BaseEntity {
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String name;
    private String category;
    private Integer sortOrder;
    private String status;
    private String remark;
}
