package com.bridgelabz.apachecameldemo;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApacheCamelDemoApplication extends RouteBuilder {

	public static void main(String[] args) {
		SpringApplication.run(ApacheCamelDemoApplication.class, args);
	}

	@Override
	public void configure() throws Exception {
		
		System.out.println("Started.....");
		//moveAllFile();
		//moveSpecificFile("first");
		
		//moveSpecificFileWithContent("Swapnil");
		//fileProcess();
		
		multiFileProcessor();
		System.out.println("End.....");
		
		
	}
	
	public void moveAllFile()
	{
		from("file:C:/Users/User/Desktop/Demo1?noop=true").to("file:C:/Users/User/Desktop/Demo2");
		
	}
	
	public void moveSpecificFile(String type)
	{
		from("file:C:/Users/User/Desktop/Demo1?noop=true").filter(header(Exchange.FILE_NAME).startsWith(type)).to("file:C:/Users/User/Desktop/Demo2");
		
	}
	
	public void moveSpecificFileWithContent(String content)
	{
		from("file:C:/Users/User/Desktop/Demo1?noop=true").filter(body().startsWith(content)).to("file:C:/Users/User/Desktop/Demo2");
		
	}
	
	public void fileProcess()
	{
		from("file:C:/Users/User/Desktop/Demo1?noop=true").process(p->{
			String body = p.getIn().getBody(String.class);
			StringBuilder sb = new StringBuilder();
			Arrays.stream(body.split(" ")).forEach(s ->{
				sb.append(s+",");});
			p.getIn().setBody(sb);
		}).to("file:C:/Users/User/Desktop/Demo2");
		
	}
	
	public void multiFileProcessor()
	{
		from("file:C:/Users/User/Desktop/Demo1?noop=true").unmarshal().csv().split(body().tokenize(",")).choice()
		.when(body().contains("closed")).to("file:C:/Users/User/Desktop/Demo2?fileName=closed.csv")
		.when(body().contains("pending")).to("file:C:/Users/User/Desktop/Demo2?fileName=pending.csv")
		.when(body().contains("interest")).to("file:C:/Users/User/Desktop/Demo2?fileName=interest.csv");
	}
	

}
