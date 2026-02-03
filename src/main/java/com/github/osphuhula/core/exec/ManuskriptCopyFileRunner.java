package com.github.osphuhula.core.exec;

import java.util.ArrayList;
import java.util.List;

import com.github.osphuhula.core.ExportConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManuskriptCopyFileRunner {

	private final ExternalToolRunner runner;

	public void copy(ExportConfig cfg) throws Exception {
		List<String> cmd = new ArrayList<>();

		cmd.add("cmd.exe");
		cmd.add("/c");
		cmd.add(cfg.projectDir() + "\\run-copy-manuskript-file.bat");
		cmd.add("--no-pause");
		int code = runner.run(cfg.projectDir(), cmd);
		if (code != 0) {
			throw new RuntimeException("Copy file failed with exit code " + code);
		}
	}

}
