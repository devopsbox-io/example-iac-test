package io.devopsbox.infrastructure.test.common.cdk


import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps

class CdkIntegrationTestStack extends Stack {
    CdkIntegrationTestStack(final Construct scope,
                            final String id,
                            final String constructClass,
                            final String propsPath) {
        this(scope, id, null, constructClass, propsPath)
    }

    CdkIntegrationTestStack(final Construct scope,
                            final String id,
                            final StackProps props,
                            final String constructClass,
                            final String propsPath) {
        super(scope, id, props)

        def constructProps = new PropsFileSerializer().deserialize(propsPath)

        Class.forName(constructClass).newInstance(this, "TestConstruct", constructProps)
    }

}
