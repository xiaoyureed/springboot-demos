package io.github.xiaoyureed.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
@RequestMapping("/resources")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Resp> demo(@RequestBody Params req) throws JsonProcessingException {
		System.out.println(">>> req = " + objectMapper.writeValueAsString(req));
		return ResponseEntity.ok(Resp.ok());
	}




	/**
	 * this config doesn't work
	 *
	 */
//	@Bean
	public WebMvcConfigurationSupport webMvcConfigurationSupport(ReqCheckInterceptor reqCheckInterceptor) {
	    return new WebMvcConfigurationSupport() {
			@Override
			protected void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(reqCheckInterceptor);
				super.addInterceptors(registry);
			}
		};
	}
//	@Bean
	public WebMvcConfigurer webMvcConfigurer(ReqCheckInterceptor interceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(interceptor);
			}
		};
	}


}
