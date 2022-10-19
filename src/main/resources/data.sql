create table results
(
    id          bigserial primary key,
    created_at  varchar(50),
    request_id  varchar(20),
    base_pinfl  varchar(20),
    check_pinfl varchar(20),
    result      boolean
);


create index resultat_base_pinfl_index
    on results (base_pinfl);


create index resultat_check_pinfl_index
    on results (check_pinfl);