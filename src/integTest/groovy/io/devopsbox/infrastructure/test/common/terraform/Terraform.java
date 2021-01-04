package io.devopsbox.infrastructure.test.common.terraform;

import io.devopsbox.infrastructure.test.common.ProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terraform {
    private static final Logger log = LoggerFactory.getLogger(Terraform.class);

    private final ProcessRunner processRunner;

    public Terraform() {
        this.processRunner = new ProcessRunner();
    }

    public void run(String moduleDirPath, String moduleVariablesFile, String terraformCommand) {
        init(moduleDirPath);

        Map<String, String> terraformEnvironmentVariables = new HashMap<>();
        List<String> commandList = new ArrayList<>();

        commandList.add("terraform");
        commandList.add(terraformCommand);
        commandList.add("-auto-approve");
        commandList.add("-state=" + moduleDirPath + "/terraform.tfstate");
        commandList.add("-var-file=" + moduleVariablesFile);
        commandList.add(moduleDirPath);

        log.debug("Starting terraform process {}", commandList);

        int returnCode = processRunner.run(
                terraformEnvironmentVariables,
                commandList.toArray(new String[0])
        );
        if (returnCode != 0) {
            throw new RuntimeException("Terraform process failed!");
        }
    }

    private void init(String moduleDirPath) {
        int returnCode = processRunner.run(
                "terraform",
                "init",
                moduleDirPath
        );
        if (returnCode != 0) {
            throw new RuntimeException("Terraform init process failed!");
        }
    }
}
