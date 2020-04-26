package org.digitalmind.buildingblocks.i18n.core.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@SuperBuilder
@Entity
@Table(
        name = "i18n",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "i18n_ux1",
                        columnNames = {"code", "locale"}
                )
        }
)
@EntityListeners({AuditingEntityListener.class})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

@ApiModel(value = "I18n", description = "Entity for providing I8n support.")
@JsonPropertyOrder(
        {
                "id", "locale", "code", "content",
                "createdAt", "createdBy", "updatedAt", "updatedBy"
        }
)

public class I18n extends ContextVersionableAuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty(value = "Unique id of the translation", required = false)
    private Long id;

    @ApiModelProperty(value = "The locale info", required = true)
    @Column(name = "locale")
    @NotNull
    private String locale;

    @ApiModelProperty(value = "The translation code", required = true)
    @Column(name = "code")
    @NotNull
    private String code;

    @ApiModelProperty(value = "The translation content", required = true)
    @Column(name = "content")
    @NotNull
    private String content;

}
