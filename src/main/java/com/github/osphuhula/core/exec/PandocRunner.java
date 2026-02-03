package com.github.osphuhula.core.exec;

import com.github.osphuhula.core.ExportConfig;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class PandocRunner {

	private final ExternalToolRunner runner;

	public void exportIndexHtmlOnly(ExportConfig cfg) throws Exception {
		List<String> cmd = new ArrayList<>();
		cmd.add(cfg.pandocExe().toString());
		cmd.add(cfg.inputMd().toString());
		cmd.add("-s");
		cmd.add("--toc");
		cmd.add("--toc-depth=" + cfg.tocDepth());
		cmd.add("--template=" + cfg.tocTemplatePath().toString());
		cmd.add("-M");
		cmd.add("title=" + cfg.title());
		cmd.add("-o");
		cmd.add(cfg.indexHtmlOut().toString());

		int code = runner.run(cfg.outDir(), cmd);
		if (code != 0)
			throw new RuntimeException("Pandoc index-html failed with exit code " + code);
	}

	public void exportMarkdownNormalized(ExportConfig cfg) throws Exception {
		List<String> cmd = List.of(cfg.pandocExe().toString(), cfg.inputMd().toString(), "-t", "gfm", "-o",
				cfg.mdOut().toString());
		int code = runner.run(cfg.outDir(), cmd);
		if (code != 0)
			throw new RuntimeException("Pandoc md failed with exit code " + code);
	}

	public void exportTxt(ExportConfig cfg) throws Exception {
		List<String> cmd = List.of(cfg.pandocExe().toString(), cfg.inputMd().toString(), "-t", "plain", "-o",
				cfg.txtOut().toString());
		int code = runner.run(cfg.outDir(), cmd);
		if (code != 0)
			throw new RuntimeException("Pandoc txt failed with exit code " + code);
	}

	public void exportHtml(ExportConfig cfg) throws Exception {
		List<String> cmd = List.of(cfg.pandocExe().toString(), cfg.inputMd().toString(), "-s", "-o",
				cfg.htmlOut().toString());
		int code = runner.run(cfg.outDir(), cmd);
		if (code != 0)
			throw new RuntimeException("Pandoc html failed with exit code " + code);
	}

	public void exportTex(ExportConfig cfg) throws Exception {
		List<String> cmd = List.of(cfg.pandocExe().toString(), cfg.inputMd().toString(), "-s", "-o",
				cfg.texOut().toString());
		int code = runner.run(cfg.outDir(), cmd);
		if (code != 0)
			throw new RuntimeException("Pandoc tex failed with exit code " + code);
	}
}
