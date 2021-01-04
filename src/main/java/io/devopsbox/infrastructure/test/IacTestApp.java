package io.devopsbox.infrastructure.test;

import software.amazon.awscdk.core.App;

public class IacTestApp {
    public static void main(final String[] args) {
        App app = new App();

        new IacTestStack(app, "IacTestStack");

        app.synth();
    }
}
