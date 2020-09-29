package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ErrorEmbeddable {

    private String errorCode;
    
    private String errorMessage;}
