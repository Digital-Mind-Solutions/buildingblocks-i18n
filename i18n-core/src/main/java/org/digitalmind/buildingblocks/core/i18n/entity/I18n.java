package org.digitalmind.buildingblocks.core.i18n.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static org.digitalmind.buildingblocks.core.i18n.entity.I18n.TABLE_NAME;

@SuperBuilder
@Entity
@Table(
        name = TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = TABLE_NAME + "_ux1",
                        columnNames = {"namespace", "code", "locale"}
                )
        },
        indexes = {
                @Index(name = TABLE_NAME + "_ix1", columnList = "namespace, locale")
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
                "id", "namespace", "code", "locale", "content",
                "createdAt", "createdBy", "updatedAt", "updatedBy"
        }
)
public class I18n extends ContextVersionableAuditModel {

    public static final String TABLE_NAME = "i18n";
    public static final String DEFAULT_NAMESPACE = "default";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(description = "Unique id of the translation")
    private Long id;

    @Schema(description = "The translation namespace", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "namespace", length = 128, nullable = false)
    @NotNull
    @Builder.Default
    private String namespace = DEFAULT_NAMESPACE;

    @Schema(description = "The translation code", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "code", length = 256, nullable = false)
    @NotNull
    private String code;

    @Schema(description = "The locale info", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "locale", length = 32, nullable = false)
    @NotNull
    private String locale;

    @Schema(description = "The translation content", requiredMode = Schema.RequiredMode.REQUIRED)
    @JdbcTypeCode(SqlTypes.CLOB)
    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    @NotNull
    private String content;

}
