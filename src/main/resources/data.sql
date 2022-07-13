INSERT INTO public.audience (number)
VALUES (12);
INSERT INTO public.audience (number)
VALUES (34);
INSERT INTO public.audience (number)
VALUES (5);
INSERT INTO public.audience (number)
VALUES (2);
INSERT INTO public.audience (number)
VALUES (23);

INSERT INTO public.groups (number)
VALUES (543);
INSERT INTO public.groups (number)
VALUES (765);
INSERT INTO public.groups (number)
VALUES (231);

INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Vasilii', 'Rogatkin', 1);
INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Evgenii', 'Molodov', 1);
INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Andrei', 'Vasiliev', 2);
INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Nikolai', 'Andreev', 2);
INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Olga', 'Shilyak', 3);
INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Elena', 'Samoilova', 3);
INSERT INTO public.student (first_name, last_name, group_id)
VALUES ('Aristarh', 'Petrov', 3);

INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture1', '2022-07-08', 1, 1);
INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture2', '2022-07-09', 1, 2);
INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture3', '2022-07-10', 2, 3);
INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture4', '2022-07-11', 2, 3);
INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture5', '2022-07-12', 2, 4);
INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture6', '2022-07-13', 3, 4);
INSERT INTO public.lecture (name, date, group_id, audience_id)
VALUES ('lecture7', '2022-07-14', 3, 5);