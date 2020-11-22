package io.github.xiaoyureed.springbootinterceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@RestController
public class SpringBootInterceptorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootInterceptorApplication.class, args);
	}

	@Autowired
	private AccMapper mapper;

	@Autowired
	private RedisService redis;

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<String> auth(@RequestBody Account req) {
		Account exit = mapper.selectOne(new QueryWrapper<Account>()
				.select("id").eq("name", req.getName()).eq("pwd", req.getPwd()));
		if (exit == null) {
			return ResponseEntity.ok("auth failed");
		}
		redis.put(UUID.randomUUID().toString(), exit);
		return ResponseEntity.ok(exit.getId().toString());
	}


	@Bean
	public ApplicationRunner testH2() {
		return new ApplicationRunner() {
			@Override
			public void run(ApplicationArguments args) throws Exception {
				List<Account> accounts = mapper.selectList(null);
				log.info(">>> accounts: {}", accounts.toString());
			}
		};
	}
}
