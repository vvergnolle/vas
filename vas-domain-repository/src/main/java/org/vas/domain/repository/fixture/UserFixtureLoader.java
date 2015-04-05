package org.vas.domain.repository.fixture;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.yaml.snakeyaml.Yaml;

public class UserFixtureLoader {

	protected final Logger logger = LoggerFactory.getLogger(UserFixtureLoader.class);
	protected Yaml yaml = new Yaml();

	@SuppressWarnings("unchecked")
	public void load(InputStream in, UserRepository userRepository) throws Exception  {
		if (logger.isDebugEnabled()) {
			logger.debug("Load default users");
		}

		Iterable<Object> iterable = (Iterable<Object>) yaml.load(in);
		userRepository.callBatchTasks(() -> {
			iterable.forEach(o -> {
				User user = (User) o;
				user.hash();

				try {
		      userRepository.create(user);

		      if (logger.isDebugEnabled()) {
		      	logger.debug("User created : {}", user.username);
		      }
	      } catch (Exception e) {
	      	throw new RuntimeException(e);
	      }
			});
			
			return null;
		});
	}
}
