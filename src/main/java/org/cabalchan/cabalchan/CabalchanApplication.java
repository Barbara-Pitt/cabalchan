package org.cabalchan.cabalchan;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CabalchanApplication {

	public static void main(String[] args) {
		SpringApplication.run(CabalchanApplication.class, args);
	}
	@Bean
	public CommandLineRunner createUploadFolders() {
	  return (args) -> {
		//create public folders on classpath for uploads
		String[] PATHS = {
			"./public"
		};
		for (String path : PATHS) {
			File directory = new File(path);
			if (! directory.exists()){
				directory.mkdir();
			}
		}
	  };
	} 
}
