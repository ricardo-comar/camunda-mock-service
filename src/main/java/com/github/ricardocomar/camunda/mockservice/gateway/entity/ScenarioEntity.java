package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"topicName", "priority"})
}) 
public class ScenarioEntity {

    @Id
    private String scenarioId;

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false)
    private Integer priority;

    @Embedded
    @AttributeOverride(
        name = "fixedMs",
        column = @Column( name = "delay_fixedms" )
    )
    @AttributeOverride(
        name = "minMs",
        column = @Column( name = "delay_minms" )
    )
    @AttributeOverride(
        name = "maxMs",
        column = @Column( name = "delay_maxms" )
    )    
    private DelayEmbeddable delay;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ConditionEntity condition;    

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VariableEntity> variables;
}
