package com.petfabula.domain.aggregate.identity.service.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendEmailRequest {

    String address;

    String title;

    String content;
}
