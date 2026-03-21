package com.esdc.lab1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = NON_NULL,content = NON_EMPTY)
public record User(Long id,
                   boolean is_bot,
                   String first_name,
                   String last_name,
                   String username,
                   String language_code,
                   boolean is_premium,
                   boolean added_to_attachment_menu,
                   boolean can_join_groups,
                   boolean can_read_all_group_messages,
                   boolean supports_inline_queries,
                   boolean can_connect_to_business,
                   boolean has_main_web_app,
                   boolean has_topics_enabled,
                   boolean allows_users_to_create_topics) {}

