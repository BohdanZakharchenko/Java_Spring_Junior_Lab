package edu.nung.se;

import edu.nung.se.resources.HelloResource;
import edu.nung.se.resources.UserResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class dropwizardAppApplication extends Application<dropwizardAppConfiguration> {

    public static void main(final String[] args) throws Exception {
        new dropwizardAppApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizardApp";
    }

    @Override
    public void initialize(final Bootstrap<dropwizardAppConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final dropwizardAppConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new UserResource());
    }

}
