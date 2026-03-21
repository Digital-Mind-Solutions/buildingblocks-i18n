package org.digitalmind.buildingblocks.core.i18n.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

@Schema(description = "Entity for providing I18n support.")
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
    @Schema(description = "Unique id of the translation")
    private Long id;

    @Schema(description = "The locale info", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "locale")
    @NotNull
    private String locale;

    @Schema(description = "The translation code", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "code")
    @NotNull
    private String code;

    @Schema(description = "The translation content", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "content")
    @NotNull
    private String content;

}
