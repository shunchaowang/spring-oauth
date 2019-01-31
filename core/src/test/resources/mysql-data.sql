# all clients have secret as client secret
insert into oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
  value ("client1", "$2a$10$L3cKmG.fiN8V2Tenw2oaDOc.3SlxyncnJwUOqLRS/x1dKOsIk29c2", "all,read,write",
         "password,authorization_code,refresh_token", "http://client1.com", null, 36000, 360000,
         null,
         true);

insert into oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
  value ("client2", "$2a$10$L3cKmG.fiN8V2Tenw2oaDOc.3SlxyncnJwUOqLRS/x1dKOsIk29c2",
         "all,read,write",
         "password,authorization_code,refresh_token", "http://example.com", null, 36000, 360000,
         null,
         false);
insert into oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
  value ("client3", "$2a$10$L3cKmG.fiN8V2Tenw2oaDOc.3SlxyncnJwUOqLRS/x1dKOsIk29c2",
         "all,read,write",
         "password,authorization_code,refresh_token", "http://example.com", null, 36000, 360000,
         null,
         true);