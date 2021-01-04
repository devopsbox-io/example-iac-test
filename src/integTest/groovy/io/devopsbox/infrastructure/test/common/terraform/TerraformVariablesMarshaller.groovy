package io.devopsbox.infrastructure.test.common.terraform

import groovy.json.JsonGenerator

class TerraformVariablesMarshaller {

    JsonGenerator generator

    TerraformVariablesMarshaller() {
        this.generator = new JsonGenerator.Options()
                .addConverter(TerraformVariables, { TerraformVariables p ->
                    p.properties
                            .findAll { it.key != 'class' }
                            .collectEntries { k, v -> [k.replaceAll(/([A-Z])/, /_$1/).toLowerCase(), v] }
                })
                .build()
    }

    void marshall(File file, TerraformVariables terraformVariables) {
        def json = generator.toJson(terraformVariables)
        file.write(json)
    }
}
