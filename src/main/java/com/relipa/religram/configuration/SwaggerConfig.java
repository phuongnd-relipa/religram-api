/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.relipa.religram.controller.bean.response.SwaggerEmptyModel;
import com.relipa.religram.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
@EnableWebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    private static final String BASE_PACKAGE = "com.relipa.religram";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_NAME = "religram_auth";
    private static final String AUTHORIZATION_DESCRIPTION = "Bearer {token}";
    private static final String USER_PATTERN = "/v1/user.*";
    private static final String POST_PATTERN = "/v1/post.*";
    private static final String HASH_TAG_PATTERN = "/v1/hashtag.*";
    private static final String SEARCH_PATTERN = "/v1/search.*";
    private static final String ACTIVITY_PATTERN = "/v1/activity-feed.*";
    private static final String AUTH_CHANGE_PASSWORD_PATTERN = "/v1/auth/changepassword.*";
    private static final String AUTH_RESET_PASSWORD_PATTERN = "/v1/auth/resetpassword.*";
    private static final String AUTH_LOGOUT_PATTERN = "/v1/auth/logout.*";
    private static final Locale LOCALE = Locale.ENGLISH;

    private MessageSource messageSource;

    // Messages
    private String MESSAGE_API_INFO_TITLE;
    private String MESSAGE_API_INFO_DESCRIPTION;
    private String MESSAGE_API_INFO_VERSION;
    private String MESSAGE_API_INFO_TERMS;
    private String MESSAGE_API_INFO_CONTACT_NAME;
    private String MESSAGE_API_INFO_CONTACT_URL;
    private String MESSAGE_API_INFO_CONTACT_EMAIL;
    private String MESSAGE_API_INFO_LICENSE_NAME;
    private String MESSAGE_API_INFO_LICENSE_URL;

    private String MESSAGE_TAG_USER;
    private String MESSAGE_TAG_POST;
    private String MESSAGE_TAG_AUTH;
    private String MESSAGE_TAG_HASH_TAG;
    private String MESSAGE_TAG_SEARCH;
    private String MESSAGE_TAG_ACTIVITY;

    private String MESSAGE_RESPONSE_400;
    private String MESSAGE_RESPONSE_401;
    private String MESSAGE_RESPONSE_403;
    private String MESSAGE_RESPONSE_404;
    private String MESSAGE_RESPONSE_500;

    @Autowired
    public SwaggerConfig(MessageSource messageSource) {
        this.messageSource = messageSource;

        MESSAGE_API_INFO_TITLE = messageSource.getMessage("api.info.title", null, LOCALE);
        MESSAGE_API_INFO_DESCRIPTION = messageSource.getMessage("api.info.description", null, LOCALE);
        MESSAGE_API_INFO_VERSION = messageSource.getMessage("api.info.version", null, LOCALE);
        MESSAGE_API_INFO_TERMS = messageSource.getMessage("api.info.terms", null, LOCALE);
        MESSAGE_API_INFO_CONTACT_NAME = messageSource.getMessage("api.info.contact.name", null, LOCALE);
        MESSAGE_API_INFO_CONTACT_URL = messageSource.getMessage("api.info.contact.url", null, LOCALE);
        MESSAGE_API_INFO_CONTACT_EMAIL = messageSource.getMessage("api.info.contact.email", null, LOCALE);
        MESSAGE_API_INFO_LICENSE_NAME = messageSource.getMessage("api.info.license.name", null, LOCALE);
        MESSAGE_API_INFO_LICENSE_URL = messageSource.getMessage("api.info.license.url", null, LOCALE);

        MESSAGE_TAG_USER = messageSource.getMessage("user.api", null, LOCALE);
        MESSAGE_TAG_POST = messageSource.getMessage("post.api", null, LOCALE);
        MESSAGE_TAG_AUTH = messageSource.getMessage("auth.api", null, LOCALE);
        MESSAGE_TAG_HASH_TAG = messageSource.getMessage("hashTag.api", null, LOCALE);
        MESSAGE_TAG_SEARCH = messageSource.getMessage("search.api", null, LOCALE);
        MESSAGE_TAG_ACTIVITY = messageSource.getMessage("activity.api", null, LOCALE);

        MESSAGE_RESPONSE_400 = messageSource.getMessage("response.message.400", null, LOCALE);
        MESSAGE_RESPONSE_401 = messageSource.getMessage("response.message.401", null, LOCALE);
        MESSAGE_RESPONSE_403 = messageSource.getMessage("response.message.403", null, LOCALE);
        MESSAGE_RESPONSE_404 = messageSource.getMessage("response.message.404", null, LOCALE);
        MESSAGE_RESPONSE_500 = messageSource.getMessage("response.message.500", null, LOCALE);
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().docExpansion(DocExpansion.LIST).build();
    }

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .additionalModels(
                        typeResolver.resolve(User.class),
                        typeResolver.resolve(Post.class),
                        typeResolver.resolve(Photo.class),
                        typeResolver.resolve(Comment.class),
                        typeResolver.resolve(Hashtag.class),
                        typeResolver.resolve(Like.class),
                        typeResolver.resolve(Follow.class),
                        typeResolver.resolve(FacebookUser.class)
                )
                .tags(
                        new Tag("user", MESSAGE_TAG_USER),
                        new Tag("post", MESSAGE_TAG_POST),
                        new Tag("auth", MESSAGE_TAG_AUTH),
                        new Tag("hashTag", MESSAGE_TAG_HASH_TAG),
                        new Tag("search", MESSAGE_TAG_SEARCH),
                        new Tag("activity", MESSAGE_TAG_ACTIVITY)
                )
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages())
                .globalResponseMessage(RequestMethod.PATCH, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages())
                .directModelSubstitute(SwaggerEmptyModel.class, Void.class)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .produces(new HashSet<>(Arrays.asList("application/json")))
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .apiInfo(apiInfo());
    }

    private List<ResponseMessage> responseMessages() {
        return new ArrayList<>(Arrays.asList(
                new ResponseMessageBuilder().code(400).message(MESSAGE_RESPONSE_400).build(),
                new ResponseMessageBuilder().code(401).message(MESSAGE_RESPONSE_401)
                        .headersWithDescription(new HashMap<String, Header>() {{
                            put(AUTHORIZATION_HEADER, new Header(AUTHORIZATION_HEADER, AUTHORIZATION_DESCRIPTION, new ModelRef("string")));
                        }})
                        .build(),
                new ResponseMessageBuilder().code(403).message(MESSAGE_RESPONSE_403).build(),
                new ResponseMessageBuilder().code(404).message(MESSAGE_RESPONSE_404).build(),
                new ResponseMessageBuilder().code(500).message(MESSAGE_RESPONSE_500).build()
        ));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(Predicates.or(
                        PathSelectors.regex(USER_PATTERN),
                        PathSelectors.regex(POST_PATTERN),
                        PathSelectors.regex(HASH_TAG_PATTERN),
                        PathSelectors.regex(SEARCH_PATTERN),
                        PathSelectors.regex(ACTIVITY_PATTERN),
                        PathSelectors.regex(AUTH_CHANGE_PASSWORD_PATTERN),
                        PathSelectors.regex(AUTH_RESET_PASSWORD_PATTERN),
                        PathSelectors.regex(AUTH_LOGOUT_PATTERN))
                )
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(new SecurityReference(AUTHORIZATION_NAME, scopes()));
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything"),
        };
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_NAME, AUTHORIZATION_HEADER, "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                MESSAGE_API_INFO_TITLE,
                MESSAGE_API_INFO_DESCRIPTION,
                MESSAGE_API_INFO_VERSION,
                MESSAGE_API_INFO_TERMS,
                new Contact(MESSAGE_API_INFO_CONTACT_NAME, MESSAGE_API_INFO_CONTACT_URL, MESSAGE_API_INFO_CONTACT_EMAIL),
                MESSAGE_API_INFO_LICENSE_NAME, MESSAGE_API_INFO_LICENSE_URL, Collections.emptyList());
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
            }
        };
    }

    @Bean
    public TranslationOperationBuilderPlugin translationPlugin() {
        return new TranslationOperationBuilderPlugin();
    }

    public class TranslationOperationBuilderPlugin implements OperationBuilderPlugin {

        @Override
        public boolean supports(DocumentationType delimiter) {
            return true;
        }

        @Override
        public void apply(OperationContext context) {
            Set<ResponseMessage> messages = context.operationBuilder().build().getResponseMessages();
            Set<ResponseMessage> translated = new HashSet<>();
            for (ResponseMessage untranslated : messages) {
                String translation = messageSource.getMessage(untranslated.getMessage(), null, untranslated.getMessage(), LOCALE);
                translated.add(new ResponseMessage(untranslated.getCode(),
                        translation,
                        untranslated.getResponseModel(),
                        untranslated.getHeaders(), null));

            }
            context.operationBuilder().responseMessages(translated);
        }

    }
}
