package com.logesh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	AsyncService async;

	@GetMapping(value = "health/{name}")
	public String getSuccessMsg(@PathVariable("name") String name) {

//		try {
//			generateFile();
//		} catch (TemplateNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedTemplateNameException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TemplateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Req for name: " + name + new Date() + " Thread:" + Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Hello " + name.toUpperCase() + " !, Welcome to our Page!!";
	}

	private void generateFile() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException,
			IOException, TemplateException {
		// 1. Configure FreeMarker
		//
		// You should do this ONLY ONCE, when your application starts,
		// then reuse the same Configuration object elsewhere.

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

		// Where do we load the templates from:
		cfg.setClassForTemplateLoading(RestController.class, "/templates/");

		// Some other recommended settings:
		cfg.setIncompatibleImprovements(new Version(2, 3, 23));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// 2. Proccess template(s)
		//
		// You will do this for several times in typical applications.

		// 2.1. Prepare the template input:

		Map<String, Object> input = new HashMap<String, Object>();

		input.put("userName", "Logesh S");

		Car c1 = new Car("Audi", 52642);
		Car c2 = new Car("Volvo", 29000);
		Car c3 = new Car("Skoda", 9000);

		List<Car> cars = new ArrayList<>();
		cars.add(c1);
		cars.add(c2);
		cars.add(c3);

		input.put("cars", cars);

		// 2.2. Get the template

		Template template = cfg.getTemplate("/file-template.ftl");

		// 2.3. Generate the output

		// Write output to the console
		Writer consoleWriter = new OutputStreamWriter(System.out);
		template.process(input, consoleWriter);

		// For the sake of example, also write output into a file:
		Writer fileWriter = new FileWriter(new File(
				"C:\\Users\\Logesh\\git\\restcicdtest\\RestCiCdTestApp\\src\\main\\resources\\op-files\\output.txt"));
		try {
			template.process(input, fileWriter);
		} finally {
			fileWriter.close();
		}

	}

	@GetMapping(value = "greet")
	public String greetUser(@RequestParam("name") String name) {
		async.callAsyncMtd();
		return "Hello ! " + name.toUpperCase() + " ! Thank you for your visit!!";
	}

	@GetMapping(value = "list")
	public Stream<Path> listFiles() throws IOException {
//		FileVisitOption FileVisitOption.FOLLOW_LINKS;
		return Files.walk(Paths.get("D:\\My works\\File-Walking"), FileVisitOption.FOLLOW_LINKS);
	}
}
