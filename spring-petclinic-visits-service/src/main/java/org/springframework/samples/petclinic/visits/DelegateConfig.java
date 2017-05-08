package org.springframework.samples.petclinic.visits;

import java.util.Arrays;

import org.hdiv.config.annotation.ExclusionRegistry;
import org.hdiv.config.annotation.RuleRegistry;
import org.hdiv.config.annotation.ValidationConfigurer;
import org.hdiv.ee.config.SessionType;
import org.hdiv.ee.config.SingleCacheConfig;
import org.hdiv.ee.config.annotation.ExternalStateStorageConfigurer;
import org.hdiv.ee.session.cache.CacheType;
import org.hdiv.filter.ValidatorFilter;
import org.hdiv.listener.InitListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableEntityLinks;

import com.hdivsecurity.services.config.EnableHdiv4ServicesSecurityConfiguration;
import com.hdivsecurity.services.config.HdivServicesSecurityConfigurerAdapter;
import com.hdivsecurity.services.config.ServicesConfig.IdProtectionType;
import com.hdivsecurity.services.config.ServicesSecurityConfigBuilder;

@Configuration
@EnableHdiv4ServicesSecurityConfiguration
@EnableEntityLinks
public class DelegateConfig extends HdivServicesSecurityConfigurerAdapter {

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		ValidatorFilter validatorFilter = new ValidatorFilter();
		registrationBean.setFilter(validatorFilter);
		registrationBean.setOrder(0);

		return registrationBean;
	}

	@Bean
	public InitListener initListener() {
		return new InitListener();
	}

	@Override
	public void configure(final ServicesSecurityConfigBuilder builder) {
		builder.confidentiality(false).sessionExpired().homePage("/");
		builder.showErrorPageOnEditableValidation(true);
		builder.reuseExistingPageInAjaxRequest(true);
		builder.idProtection(IdProtectionType.PLAINTEXT_HID).sessionType(SessionType.STATELESS);
		builder.hypermediaSupport(false).csrfHeader(false);
	}

	@Override
	public void configureExternalStateStorage(final ExternalStateStorageConfigurer externalStateStorageConfigurer) {

		SingleCacheConfig config = new SingleCacheConfig(CacheType.EXT_NO_SQL);
		externalStateStorageConfigurer.mongoExternalStateStore().host("127.0.0.1").port(27017);
		externalStateStorageConfigurer.cacheConfig(Arrays.asList(config));

		super.configureExternalStateStorage(externalStateStorageConfigurer);
	}

	@Override
	public void addExclusions(final ExclusionRegistry registry) {
		registry.addUrlExclusions("/", "/info", "/health", "/scripts/.*", "/bootstrap/.*", "/images/.*", "/fonts/.*",
				"/angular-ui-router/.*", "/angular/.*", "/angular-cookies/.*", "/jquery/.*", "/css/.*");
	}

	@Override
	public void addRules(final RuleRegistry registry) {
		registry.addRule("safeText").acceptedPattern("^[a-zA-Z0-9 :@.\\-_+#]*$").rejectedPattern("(\\s|\\S)*(--)(\\s|\\S)*]");
		registry.addRule("numbers").acceptedPattern("^[1-9]\\d*$");
	}

	@Override
	public void configureEditableValidation(final ValidationConfigurer validationConfigurer) {
		validationConfigurer.addValidation("/.*").forParameters("amount").rules("numbers").disableDefaults();
		validationConfigurer.addValidation("/.*").rules("safeText").disableDefaults();
	}

}
