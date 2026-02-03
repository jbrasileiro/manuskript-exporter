package com.github.osphuhula.core.exec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class ExternalToolRunner {

    public int run(Path workingDir, List<String> command) throws Exception {
        log.info("RUN: {}", String.join(" ", command));

        ProcessBuilder pb = new ProcessBuilder(command);
        if (workingDir != null) pb.directory(workingDir.toFile());
        pb.redirectErrorStream(true);

        Process p = pb.start();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                log.info(line);
            }
        }

        int code = p.waitFor();
        log.info("EXIT CODE: {}", code);
        return code;
    }
}
