package com.github.osphuhula.core.exec;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.osphuhula.core.ExportConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class FileContentAssemblerRunner {

    
	private static final String TARGET_FILE_NOME = "bunble-all.txt";
	
    private static final Set<String> IGNORED_FILES = Set.of(
            TARGET_FILE_NOME
    );


	public void assemble(ExportConfig cfg) {
		try {
			consolidateContent(cfg.manuskriptFileDir(), "bunble-all.txt");
			
			Path inputDirP01 = cfg.manuskriptFileDir().resolve("\\P01"); 
			if (hasFile(inputDirP01)) {
				consolidateContent(inputDirP01, "bunble-all.txt");
			}
			
			Path inputDirP02 = cfg.manuskriptFileDir().resolve("\\P02");
			if (hasFile(inputDirP02)) {
				consolidateContent(inputDirP02, "bunble-all.txt");
			}
		} catch (Exception e) {
			System.err.println("[ERRO] " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private boolean hasFile(Path directory) throws IOException {
		if(Files.exists(directory) && !Files.isDirectory(directory)) {
            throw new IllegalStateException("Não é um diretorio : " + directory.toAbsolutePath());
		}
		return Files.exists(directory) 
				&& !Files.list(directory).toList().isEmpty();
	}

	private void consolidateContent(Path directory, String filename) throws IOException {
		log.info("Assembling files in " + directory.toString() + " to " + filename);
		if (!Files.isDirectory(directory)) {
            throw new IllegalStateException(
                    "Pasta de entrada não encontrada: " + directory.toAbsolutePath());
        }

        List<Path> files;
        try (Stream<Path> stream = Files.list(directory)) {
            files = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .filter(p -> !IGNORED_FILES.contains(
                            p.getFileName().toString().toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        if (files.isEmpty()) {
            throw new IllegalStateException("Nenhum arquivo .txt válido encontrado em " + directory);
        }
        
        System.out.println(files
        		.stream()
        		.map(each -> each.getFileName().toString())
        		.collect(Collectors.joining("\n")));
        
        Path output = Paths.get(directory.toAbsolutePath() + "\\" + filename);
		Files.createDirectories(output.toAbsolutePath().getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(
        		output,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Path file : files) {
                writer.write("----- BEGIN FILE:" + file.getFileName() + " -----");
                writer.newLine();

                String content = Files.readString(file, StandardCharsets.UTF_8);
                writer.write(content);

                if (!content.endsWith("\n")) {
                    writer.newLine();
                }

                writer.write("----- END FILE:" + file.getFileName() + " -----");
                writer.newLine();
                writer.newLine();
            }
        }

        System.out.println("[OK] Arquivo gerado: " + output.toAbsolutePath());
        System.out.println("[OK] Arquivos processados: " + files.size());
        System.out.println("\n\n");
	}

}
